package com.tistory.mybstory.firechat.ui.settings

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override var backPressedCallback: (OnBackPressedCallback.() -> Unit)? = null

}
