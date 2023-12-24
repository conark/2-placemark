package ie.wit.finddoctor.ui.provider

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.finddoctor.R
import ie.wit.finddoctor.databinding.FragmentProviderBinding
import ie.wit.finddoctor.models.ProviderModel
import ie.wit.finddoctor.ui.auth.LoggedInViewModel
import ie.wit.finddoctor.ui.map.MapsViewModel
import ie.wit.finddoctor.ui.report.ReportViewModel


class ProviderFragment : Fragment() {

    var total = 0
    private var _fragBinding: FragmentProviderBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var providerViewModel: ProviderViewModel
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _fragBinding = FragmentProviderBinding.inflate(inflater, container, false)
        val root = fragBinding.root
	 setupMenu()
        providerViewModel = ViewModelProvider(this).get(ProviderViewModel::class.java)
        providerViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

//        fragBinding.progressBar.max = 10000
//        fragBinding.drAmountPicker.minValue = 1
//        fragBinding.drAmountPicker.maxValue = 1000
//
//        fragBinding.drAmountPicker.setOnValueChangedListener { _, _, newVal ->
//            //Display the newly selected number to DrAmount
//            fragBinding.drAmount.setText("$newVal")
//        }
        setButtonListener(fragBinding)

        return root;
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.providerError),Toast.LENGTH_LONG).show()
        }
    }

    private fun setButtonListener(layout: FragmentProviderBinding) {
        layout.providerButton.setOnClickListener {
//            val  drAmount = if (layout.drAmount.text.isNotEmpty())
//                layout.drAmount.text.toString().toInt() else layout.drAmountPicker.value


            val providerName = layout.providerName.text.toString()
            val providerDrName = layout.providerDrName.text.toString()
            val providerAddress = layout.providerAddress.text.toString()
            val providerCity = layout.providerCity.text.toString()

//            if(total >= layout.progressBar.max)
//                Toast.makeText(context,"Total Amount Exceeded!", Toast.LENGTH_LONG).show()
//            else {
//                val providertype = if(layout.providerType.checkedRadioButtonId == R.id.GP) "GP" else "Consultant"
            val providertype = when {
                layout.providerType.checkedRadioButtonId == R.id.GP -> "GP"
                layout.providerType.checkedRadioButtonId == R.id.consultant -> "Consultant"
                layout.providerType.checkedRadioButtonId == R.id.dentist -> "Dentist"
                else -> "Unknown"
            }
//                total += amount
//                layout.totalSoFar.text = String.format(getString(R.string.totalSoFar),total)
//                layout.progressBar.progress = total
                providerViewModel.addProvider(loggedInViewModel.liveFirebaseUser,
                    ProviderModel(providerName = providerName, providerDrName = providerDrName, providertype =  providertype,
//                        drAmount = drAmount,
                        providerAddress = providerAddress ,
                        providerCity = providerCity ,
                        email = loggedInViewModel.liveFirebaseUser.value?.email!!,
                        latitude = mapsViewModel.currentLocation.value!!.latitude,
                        longitude = mapsViewModel.currentLocation.value!!.longitude))
                 }
  //      }
    }
private fun setupMenu() {
    (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
        override fun onPrepareMenu(menu: Menu) {
            // Handle for example visibility of menu items
        }

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_provider, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            // Validate and handle the selected menu item
            return NavigationUI.onNavDestinationSelected(menuItem,
                requireView().findNavController())
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

//    override fun onResume() {
//        super.onResume()
//        total = reportViewModel.observableProvidersList.value!!.sumOf { it.amount }
//        fragBinding.progressBar.progress = total
//        fragBinding.totalSoFar.text = String.format(getString(R.string.totalSoFar),total)
//    }
}