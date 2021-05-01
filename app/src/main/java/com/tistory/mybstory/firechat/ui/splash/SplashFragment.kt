package com.tistory.mybstory.firechat.ui.splash

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels()
    override var backPressedCallback: (OnBackPressedCallback.() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch {
            delay(2000)
            observeSignInStatus()
            viewModel.checkSignInStatus()
        }
    }

    private fun observeSignInStatus() {
        viewModel.signInState
            .onEach { isSignedIn ->
                Timber.e("signed in : $isSignedIn")
                if (isSignedIn) {
                    findNavController().navigate(R.id.action_splash_to_homeFragment)
                } else {
                    findNavController().navigate(R.id.action_splash_to_firstRunFragment)
                }
            }.launchIn(lifecycleScope)
    }


}
