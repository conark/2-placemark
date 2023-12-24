package ie.wit.finddoctor.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize


@IgnoreExtraProperties
@Parcelize
data class ProviderModel(
      var uid: String? = "",
      var providerName: String = "",
      var providerDrName: String = "",
//      var paymentmethod: String = "N/A",
      var drAmount: Int = 0,
//      var message: String = "",
//      var upvotes: Int = 0,
      var providertype: String = "N/A",
      var providerAddress: String = "",
      var providerCity: String = "",
      var providerCounty: String = "",
      var profilepic: String = "",
      var latitude: Double = 0.0,
      var longitude: Double = 0.0,
      var email: String? = "joe@bloggs.com")
      : Parcelable
{
      @Exclude
      fun toMap(): Map<String, Any?> {
            return mapOf(
                  "uid" to uid,
                  "providerName" to providerName,
                  "providerDrName" to providerDrName,
//                  "paymentmethod" to paymentmethod,
                  "drAmount" to drAmount,
//                  "message" to message,
//                  "upvotes" to upvotes,
                  "providertype" to providertype,
                  "providerAddress" to providerAddress,
                  "providerCity" to providerCity,
                  "providerCounty" to providerCounty,
                  "profilepic" to profilepic,
                  "latitude" to latitude,
                  "longitude" to longitude,
                  "email" to email
            )
      }
}


