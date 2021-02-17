package com.tistory.mybstory.firechat.ui.auth.phone

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.databinding.FragmentPhoneAuthBinding
import com.tistory.mybstory.firechat.ui.auth.phone.country.getCountryByName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PhoneAuthFragment : BaseFragment<FragmentPhoneAuthBinding>(R.layout.fragment_phone_auth) {

    private val viewModel: PhoneAuthViewModel by viewModels(ownerProducer = { this })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() = with(binding) {
        vm = viewModel
        btnBack.setOnClickListener { findNavController().navigateUp() }
        tvSelectedCountry.setOnClickListener { findNavController().navigate(R.id.action_phoneAuth_to_countrySelectFragment) }
        btnNext.setOnClickListener { PhoneConfirmDialog().show(childFragmentManager, "") }
        // set fragment result callback (country)
        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            val countryName = bundle.getString(BUNDLE_KEY_COUNTRY)
            setSelectedResult(countryName)
        }
    }

    private fun setSelectedResult(data: String?) {
        data?.let {
            getCountryByName(it)?.let { country ->
                viewModel.selectCountry(country)
            }
        }
    }

    companion object {
        const val REQUEST_KEY = "COUNTRY_SELECT_REQUEST"
        const val BUNDLE_KEY_COUNTRY = "COUNTRY"
    }

}
