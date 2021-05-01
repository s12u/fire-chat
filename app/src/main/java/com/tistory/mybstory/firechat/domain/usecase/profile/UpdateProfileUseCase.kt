package com.tistory.mybstory.firechat.domain.usecase.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tistory.mybstory.firechat.di.IoDispatcher
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.UseCase
import com.tistory.mybstory.firechat.domain.model.UserProfile
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<UserProfile, Boolean>(ioDispatcher) {

    @ExperimentalCoroutinesApi
    override fun execute(parameters: UserProfile): Flow<Result<Boolean>> = callbackFlow {
        Timber.e("update profile...")
        val updateDbTask = firestore.collection("test_user")
            .document(parameters.uid)
            .set(parameters, SetOptions.merge())

        val profileChangeRequest = UserProfileChangeRequest.Builder()
            .setDisplayName("exists")
            .build()

        val updateProfileTask = auth.currentUser?.updateProfile(profileChangeRequest)

        updateDbTask.continueWithTask { task->
            if (!task.isSuccessful) {
                Timber.e("profile update failed with DB")
                cancel(CancellationException("Failed to update profile with DB"))
            }
            updateProfileTask
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.e("profile update success")
                offer(Result.Success(true))
                close()
            } else {
                Timber.e("profile update failed")
                cancel(CancellationException("Failed to update profile"))
            }
        }
        offer(Result.Loading)
        awaitClose()
    }
}

