package ie.wit.finddoctor.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.finddoctor.firebase.FirebaseDBManager
import ie.wit.finddoctor.models.ProviderModel
import timber.log.Timber

class ProviderDetailViewModel : ViewModel() {
    private val provider = MutableLiveData<ProviderModel>()

    var observableProvider: LiveData<ProviderModel>
        get() = provider
        set(value) {provider.value = value.value}

    fun getProvider(userid:String, providerid: String) {
        try {
            //ProviderManager.findById(email, id, provider)
            FirebaseDBManager.findById(userid, providerid, provider)
            Timber.i("Detail getProvider() Success : ${
                provider.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getProvider() Error : $e.message")
        }
    }

    fun updateProvider(userid:String, providerid: String,provider: ProviderModel) {
        try {
            //ProviderManager.update(email, id, provider)
            FirebaseDBManager.update(userid, providerid, provider)
            Timber.i("Detail update() Success : $provider")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }

    fun delete(userid: String, providerid: String) {
        try {
            //ProviderManager.delete(userid,id)
            FirebaseDBManager.delete(userid,providerid)
            Timber.i("Detail Delete Success : $provider")
        }
        catch (e: java.lang.Exception) {
            Timber.i("Detail Delete Error : $e.message")
        }
    }
}