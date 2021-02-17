package com.tistory.mybstory.firechat.ui.auth.phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.databinding.DialogPhoneConfirmBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class PhoneConfirmDialog : DialogFragment() {

    private lateinit var binding: DialogPhoneConfirmBinding
    private val viewModel: PhoneAuthViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_phone_confirm, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() = with(binding) {
        vm = viewModel
        lifecycleOwner = viewLifecycleOwner

        btnCancel.setOnClickListener { dismiss() }
        btnConfirm.setOnClickListener {
            // TODO: check if user's phone number exists & validate phone number
            viewModel.sendVerificationCode(requireActivity())
        }

        viewModel.phoneAuthUiStateFlow
            .onEach { handleUiState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleUiState(state: PhoneAuthUiState) {
        when (state) {
            is PhoneAuthUiState.Success -> navigateToVerifyFragment()
            is PhoneAuthUiState.Error -> {
                // TODO: show error dialog or message
            }
            is PhoneAuthUiState.Loading -> {
                // TODO: show loading
                
            }
            else -> {}
        }
    }

    private fun navigateToVerifyFragment() = lifecycleScope.launch {
        val directions = PhoneAuthFragmentDirections.actionPhoneAuthToVerifyCodeFragment(
            viewModel.phoneNumberFlow.stateIn(this).value,
            viewModel.verificationDataFlow.value
        )
        requireParentFragment().findNavController().navigate(directions)
    }
}
