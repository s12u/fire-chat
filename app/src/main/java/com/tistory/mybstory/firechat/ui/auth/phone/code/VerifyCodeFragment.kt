package com.tistory.mybstory.firechat.ui.auth.phone.code

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.base.ui.hideKeyboard
import com.tistory.mybstory.firechat.databinding.FragmentVerifyCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class VerifyCodeFragment: BaseFragment<FragmentVerifyCodeBinding>(R.layout.fragment_verify_code) {

    private val viewModel: VerifyCodeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    @ExperimentalCoroutinesApi
    private fun initUI() = with(binding) {
        vm = viewModel
        btnVerify.setOnClickListener {
            viewModel.verifyPhoneNumberWithCode()
            hideKeyboard()
        }

        // TODO: show counter text
        // TODO: clear text & show dialog on expired
    }

}
