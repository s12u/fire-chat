package com.tistory.mybstory.firechat.ui.auth.phone

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.databinding.FragmentPhoneAuthBinding
import com.tistory.mybstory.firechat.ui.auth.phone.country.getCountryByName
import com.tistory.mybstory.firechat.util.Event
import com.tistory.mybstory.firechat.util.EventBus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PhoneAuthFragment : BaseFragment<FragmentPhoneAuthBinding>(R.layout.fragment_phone_auth) {

    @Inject lateinit var eventBus: EventBus
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
        viewModel.phoneAuthUiStateFlow
            .onEach { handleUiState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleUiState(state: PhoneAuthUiState) = launch {
        when (state) {
            is PhoneAuthUiState.Success -> {
                eventBus.postEvent(Event.HideProgress)
                navigateToVerifyFragment()
            }
            is PhoneAuthUiState.Error -> {
                eventBus.postEvent(Event.HideProgress)
            }
            is PhoneAuthUiState.Loading -> {
                eventBus.postEvent(Event.ShowProgress)
            }
            else -> {}
        }
    }

    private fun setSelectedResult(data: String?) {
        data?.let {
            getCountryByName(it)?.let { country ->
                viewModel.selectCountry(country)
            }
        }
    }

    private fun navigateToVerifyFragment() = lifecycleScope.launch {
        val directions = PhoneAuthFragmentDirections.actionPhoneAuthToVerifyCodeFragment(
            viewModel.phoneNumberFlow.stateIn(this).value,
            viewModel.verificationDataFlow.value
        )
        requireParentFragment().findNavController().navigate(directions)
    }

    companion object {
        const val REQUEST_KEY = "COUNTRY_SELECT_REQUEST"
        const val BUNDLE_KEY_COUNTRY = "COUNTRY"
    }

}
