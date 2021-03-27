package com.tistory.mybstory.firechat.ui.auth.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.databinding.FragmentNewProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NewProfileFragment: BaseFragment<FragmentNewProfileBinding>(R.layout.fragment_new_profile) {

    private val viewModel: NewProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() = with(binding) {
        vm = viewModel

        Glide.with(imgProfile)
            .load(R.drawable.ic_blank_profile)
            .circleCrop()
            .into(imgProfile)

        setFragmentResultListener("image_select") { _, result ->
            val resultUri = result.getString("uri")
            resultUri?.let {
                val uri = Uri.parse(it)
                Timber.e("${result.getString("uri")}")

                Glide.with(imgProfile)
                    .load(uri)
                    .circleCrop()
                    .into(imgProfile)
            }
        }

        imgProfile.setOnClickListener {
            findNavController().navigate(R.id.action_show_image_select_dialog)
        }
    }



}
