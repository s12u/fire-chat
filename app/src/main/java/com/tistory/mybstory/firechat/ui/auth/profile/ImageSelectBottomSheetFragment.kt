package com.tistory.mybstory.firechat.ui.auth.profile

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.databinding.FragmentImageSelectBottomSheetBinding
import com.tistory.mybstory.firechat.util.DateUtils
import timber.log.Timber
import java.io.File
import java.time.format.DateTimeFormatter

class ImageSelectBottomSheetFragment : BottomSheetDialogFragment() {

    private var tempImageUri: Uri? = null
    private lateinit var binding: FragmentImageSelectBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_image_select_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() = with(binding) {
        layoutSelectImage.setOnClickListener { openImagePicker() }
        layoutTakePhoto.setOnClickListener { requestCameraPermission.launch(Manifest.permission.CAMERA) }
    }

    private fun openCamera() {
//        Timber.e("uri : ${requireContext().getTempImageUri()}")
        tempImageUri = getTempImageUri()
        cameraActivityLauncher.launch(tempImageUri)
//        dismiss()
    }

    private fun openImagePicker() {
        imagePickerActivityLauncher.launch("image/*")
//        dismiss()
    }

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        }
    }

    private val cameraActivityLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        Timber.e("camera result = $isSuccess")
        if (isSuccess) {
            setFragmentResult(REQUEST_KEY, bundleOf("uri" to tempImageUri?.toString()))
        }
        dismiss()
    }

    private val imagePickerActivityLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { result ->
        result?.let { uri ->
            setFragmentResult(REQUEST_KEY, bundleOf("uri" to uri.toString()))
        }
        dismiss()
    }

    private fun getTempImageUri(): Uri = with(requireContext()) {
        val cacheImageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
        val tempFileName = DateUtils.now().format(formatter)
        val tempFile = File.createTempFile(
            tempFileName,
            ".jpg",
            cacheImageDir
        )

        return FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            tempFile
        )
    }

    companion object {
        const val REQUEST_KEY = "image_select"
    }
}

