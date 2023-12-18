package ie.wit.finddoctor.main

import android.app.Application
import timber.log.Timber

class FindDoctorApp : Application() {

    //lateinit var donationsStore: DonationStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
      //  donationsStore = DonationManager()
        Timber.i("Find Doctor Application Started")
    }
}