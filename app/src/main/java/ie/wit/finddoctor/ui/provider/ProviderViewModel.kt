package ie.wit.finddoctor.ui.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.finddoctor.firebase.FirebaseDBManager
import ie.wit.finddoctor.firebase.FirebaseImageManager
import ie.wit.finddoctor.models.ProviderModel

class ProviderViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addProvider(firebaseUser: MutableLiveData<FirebaseUser>,
                    placemark: ProviderModel) {
        status.value = try {
            //ProviderManager.create(provider)
            provider.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,provider)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}