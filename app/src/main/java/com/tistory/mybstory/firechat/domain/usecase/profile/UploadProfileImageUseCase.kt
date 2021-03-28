package com.tistory.mybstory.firechat.domain.usecase.profile

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tistory.mybstory.firechat.di.IoDispatcher
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.UseCase
import com.tistory.mybstory.firechat.domain.model.UserProfile
import com.tistory.mybstory.firechat.util.DateUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UploadProfileImageUseCase @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
): UseCase<Pair<String, String>, Boolean>(ioDispatcher) {

    @ExperimentalCoroutinesApi
    override fun execute(parameters: Pair<String, String>): Flow<Result<Boolean>> = callbackFlow {
        Timber.e("upload profile image...")
        val uid = parameters.first
        val fileName = "${DateUtils.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))}.jpg"
        val storageRef = firebaseStorage.reference
        val imageRef = storageRef.child("profile_images/${uid}/$fileName")
        val localFileUri = Uri.parse(parameters.second)
        val uploadTask = imageRef.putFile(localFileUri)

        val urlTask = uploadTask.continueWithTask { task->
            if (!task.isSuccessful) {
                cancel("Failed to upload profile image.")
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task->
            if (task.isSuccessful) {
                Timber.e("profile image upload success")
                offer(Result.Success(true))
                close()
            } else {
                cancel("Failed to get profile image url")
            }
        }
        offer(Result.Loading)
        awaitClose()
    }
}
