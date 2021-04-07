package com.tistory.mybstory.firechat.ui.auth.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tistory.mybstory.firechat.data.repository.impl.getCurrentUserId
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.model.UserProfile
import com.tistory.mybstory.firechat.domain.usecase.profile.UpdateProfileUseCase
import com.tistory.mybstory.firechat.domain.usecase.profile.UploadProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class NewProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private var selectedImageUri: String = ""
    val userNameLiveData = MutableLiveData("")
    private val _profileUpdateUiStateFlow =
        MutableStateFlow<NewProfileUiState>(NewProfileUiState.None)
    private val profileUpdateUiStateFlow: StateFlow<NewProfileUiState> get() = _profileUpdateUiStateFlow

    fun setSelectedImageUri(uri: String) {
        selectedImageUri = uri
    }

    fun performUpdateProfile() {
        val uid = firebaseAuth.getCurrentUserId()
        if (uid != null && selectedImageUri.isNotEmpty()) {

//            uploadProfileImage(uid, selectedImageUri)
//                .onEach { result ->
//                    if (result is Result.Loading) {
//                        _profileUpdateUiStateFlow.value = NewProfileUiState.Loading
//                    }
//                }.filter {
//                    it !is Result.Loading
//                }.flatMapMerge { uploadResult ->
//                    when (uploadResult) {
//                        is Result.Success -> updateProfile(uid)
//                        else -> throw Exception("")
//                    }
//                }.catch {
//                    _profileUpdateUiStateFlow.value = NewProfileUiState.Error(it)
//                    handleException(it)
//                }
//                .onEach { updateResult ->
//                    if (updateResult is Result.Success) {
//                        _profileUpdateUiStateFlow.value = NewProfileUiState.Success
//                        Timber.e("profile upload success!")
//                    }
//                }.flowOn(Dispatchers.IO)
//                .launchIn(viewModelScope)

            uploadProfileImage(uid, selectedImageUri)
                .onStart { _profileUpdateUiStateFlow.value = NewProfileUiState.Loading }
                .flatMapMerge { uploadResult->
                    when (uploadResult) {
                        is Result.Success->{ updateProfile(uid) }
                        is Result.Error -> {
                            handleException(uploadResult.exception)
                            emptyFlow()
                        }
                        else -> {
                            emptyFlow()
                        }
                    }
                }.onEach { updateResult ->
                    when (updateResult) {
                        is Result.Success ->{
                            _profileUpdateUiStateFlow.value = NewProfileUiState.Success
                            Timber.e("profile upload success!")
                        }
                        is Result.Error -> {
                            handleException(updateResult.exception)
                        }
                        else -> { }
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun uploadProfileImage(currentUid: String, uri: String): Flow<Result<Boolean>> {
        return uploadProfileImageUseCase(currentUid to uri)
    }

    private fun updateProfile(currentUid: String): Flow<Result<Boolean>> {
        val userProfile = UserProfile().apply {
            uid = currentUid
            name = userNameLiveData.value ?: ""
        }
        return updateProfileUseCase(userProfile)
    }

    private fun handleException(e: Throwable) {
        _profileUpdateUiStateFlow.value = NewProfileUiState.Error(e)
        when (e) {

        }
    }

    sealed class NewProfileUiState {
        object None : NewProfileUiState()
        object Success : NewProfileUiState()
        object Loading : NewProfileUiState()
        data class Error(val exception: Throwable) : NewProfileUiState()
    }

}
