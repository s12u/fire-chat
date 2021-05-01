package com.tistory.mybstory.firechat.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.databinding.FragmentHomeBinding
import com.tistory.mybstory.firechat.util.BackPressedCallbacks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val mainViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = mainViewModel

            btnSignOut.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
            }
        }
    }

    override var backPressedCallback: (OnBackPressedCallback.() -> Unit)? = BackPressedCallbacks.doubleBackpressToExit(this)
}
