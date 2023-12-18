package ie.wit.finddoctor.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface ProviderStore {
    fun findAll(providersList:
                MutableLiveData<List<ProviderModel>>)
    fun findAll(userid:String,
                providersList:
                MutableLiveData<List<ProviderModel>>)
    fun findById(userid:String, providerid: String,
                 provider: MutableLiveData<ProviderModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, provider: ProviderModel)
    fun delete(userid:String, providerid: String)
    fun update(userid:String, providerid: String, provider: ProviderModel)
}

