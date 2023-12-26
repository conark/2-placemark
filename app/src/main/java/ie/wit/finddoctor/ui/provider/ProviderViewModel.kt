package ie.wit.finddoctor.ui.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import ie.wit.finddoctor.firebase.FirebaseDBManager
import ie.wit.finddoctor.firebase.FirebaseImageManager
import ie.wit.finddoctor.models.ProviderModel

class ProviderViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    private val _locationLiveData = MutableLiveData<LatLng>()
    val locationLiveData: LiveData<LatLng> get() = _locationLiveData

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addProvider(firebaseUser: MutableLiveData<FirebaseUser>,
                    provider: ProviderModel) {
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