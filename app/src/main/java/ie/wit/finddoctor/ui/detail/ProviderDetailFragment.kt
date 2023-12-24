package ie.wit.finddoctor.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.finddoctor.databinding.FragmentProviderDetailBinding
import ie.wit.finddoctor.ui.auth.LoggedInViewModel
import ie.wit.finddoctor.ui.report.ReportViewModel
import timber.log.Timber


class ProviderDetailFragment : Fragment() {

    private lateinit var detailViewModel: ProviderDetailViewModel
    private val args by navArgs<ProviderDetailFragmentArgs>()
    private var _fragBinding: FragmentProviderDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentProviderDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(ProviderDetailViewModel::class.java)
        detailViewModel.observableProvider.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editProviderButton.setOnClickListener {
            detailViewModel.updateProvider(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.providerid, fragBinding.providervm?.observableProvider!!.value!!)
            findNavController().navigateUp()
        }

//        fragBinding.deleteProviderButton.setOnClickListener {
//            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
//                detailViewModel.observableProvider.value?.uid!!)
//            findNavController().navigateUp()
//        }
        fragBinding.deleteProviderButton.setOnClickListener {
            try {
                reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                    detailViewModel.observableProvider.value?.uid!!)
                findNavController().navigateUp()
            } catch (e: Exception) {
                Timber.e("プロバイダの削除中にエラーが発生しました: ${e.message}")
            }
        }

        return root
    }

    private fun render() {
        fragBinding.editProviderDrName.setText("Dr Name")
        fragBinding.editProviderAddress.setText("address")
        fragBinding.providervm = detailViewModel
        Timber.i("Retrofit fragBinding.providervm == $fragBinding.providervm")
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getProvider(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.providerid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}