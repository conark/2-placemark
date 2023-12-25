package ie.wit.finddoctor.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.finddoctor.models.ProviderModel
import ie.wit.finddoctor.models.ProviderStore
import timber.log.Timber

object FirebaseDBManager : ProviderStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(providersList: MutableLiveData<List<ProviderModel>>) {
        database.child("providers")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Provider error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<ProviderModel>()
                    val children = snapshot.children
                    children.forEach {
                        val provider = it.getValue(ProviderModel::class.java)
                        localList.add(provider!!)
                    }
                    database.child("providers")
                        .removeEventListener(this)

                    providersList.value = localList
                }
            })
    }

    override fun findAll(userid: String, providersList: MutableLiveData<List<ProviderModel>>) {

        database.child("user-providers").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Provider error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<ProviderModel>()
                    val children = snapshot.children
                    children.forEach {
                        val provider = it.getValue(ProviderModel::class.java)
                        localList.add(provider!!)
                    }
                    database.child("user-providers").child(userid)
                        .removeEventListener(this)

                    providersList.value = localList
                }
            })
    }

    override fun findById(userid: String, providerid: String, provider: MutableLiveData<ProviderModel>) {

        database.child("user-providers").child(userid)
            .child(providerid).get().addOnSuccessListener {
                provider.value = it.getValue(ProviderModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, provider: ProviderModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("providers").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        provider.uid = key
        val providerValues = provider.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/providers/$key"] = providerValues
        childAdd["/user-providers/$uid/$key"] = providerValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, providerid: String) {
        Timber.i("delete メソッドが呼び出されました。UserID: $userid, ProviderID: $providerid")
        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/providers/$providerid"] = null
        childDelete["/user-providers/$userid/$providerid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, providerid: String, provider: ProviderModel) {

        val providerValues = provider.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["providers/$providerid"] = providerValues
        childUpdate["user-providers/$userid/$providerid"] = providerValues

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String,imageUri: String) {

        val userProviders = database.child("user-providers").child(userid)
        val allProviders = database.child("providers")

        userProviders.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all providers that match 'it'
                        val provider = it.getValue(ProviderModel::class.java)
                        allProviders.child(provider!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }

    fun searchByKeyword(keyword: String, providersList: MutableLiveData<List<ProviderModel>>) {
        val query = database.child("providers")
            .orderByChild("providerName")
            .startAt(keyword)
            .endAt(keyword + "\uf8ff") // \uf8ff Unicode's last word

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.i("Firebase Provider error: ${error.message}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val localList = ArrayList<ProviderModel>()
                snapshot.children.forEach {
                    val provider = it.getValue(ProviderModel::class.java)
                    if (provider != null && provider.providerName.contains(keyword, ignoreCase = true)) {
                        localList.add(provider)
                    }
                }

                providersList.value = localList
            }
        })
    }
}