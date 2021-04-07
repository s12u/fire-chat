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
            navigateToVerificationFragment()
            dismiss()
        }
    }

    private fun navigateToVerificationFragment() = lifecycleScope.launch {
        val parentFragment = requireParentFragment()
        val phoneNumber = viewModel.phoneNumberFlow.stateIn(this).value
        val directions = PhoneAuthFragmentDirections.actionPhoneAuthToVerifyCodeFragment(phoneNumber)
        parentFragment.findNavController().navigate(directions)
    }
}
