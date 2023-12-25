package ie.wit.finddoctor.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.finddoctor.firebase.FirebaseDBManager
import ie.wit.finddoctor.models.ProviderModel
import timber.log.Timber
import java.lang.Exception

class ReportViewModel : ViewModel() {

    private val providersList =
        MutableLiveData<List<ProviderModel>>()
    var readOnly = MutableLiveData(false)

    val observableProvidersList: LiveData<List<ProviderModel>>
        get() = providersList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init { load() }

    fun load() {
        try {
            //ProviderManager.findAll(liveFirebaseUser.value?.email!!, providersList)
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,providersList)
            Timber.i("Report Load Success : ${providersList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(providersList)
            Timber.i("Report LoadAll Success : ${providersList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun delete(userid: String, providerid: String) {
        try {
            //ProviderManager.delete(userid,id)
            FirebaseDBManager.delete(userid,providerid)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

    fun searchProvidersByKeyword(keyword: String) {
        try {
            FirebaseDBManager.searchByKeyword(keyword, providersList)
            Timber.i("Search Success : ${providersList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Search Error : $e.message")
        }
    }


}

