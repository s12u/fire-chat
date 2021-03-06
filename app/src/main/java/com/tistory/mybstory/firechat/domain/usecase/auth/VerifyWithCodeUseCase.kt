package com.tistory.mybstory.firechat.domain.usecase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.tistory.mybstory.firechat.di.IoDispatcher
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class VerifyWithCodeUseCase @Inject constructor(
    private val auth: FirebaseAuth,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<Pair<String, String>, PhoneNumberVerifyResult>(ioDispatcher) {
    @ExperimentalCoroutinesApi
    override fun execute(parameters: Pair<String, String>): Flow<Result<PhoneNumberVerifyResult>> {
        return callbackFlow {
            Timber.e("given: $parameters")
            val (verificationId, code) = parameters
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            offer(Result.Loading)
            auth.signInWithCredential(credential).addOnCompleteListener { task->
                if (task.isSuccessful) {
                    val isNewUser = task.result?.user?.displayName.isNullOrEmpty()
                    offer(Result.Success(PhoneNumberVerifyResult(true, credential, isNewUser)))
                    close()
                } else {
                    Timber.e("verification exception : ${task.exception}")
                    offer(Result.Error(task.exception ?: Error("Code verification failed.")))
                    close()
                }
            }
            awaitClose()
        }
    }
}

data class PhoneNumberVerifyResult(
    val isValid: Boolean,
    val credential: PhoneAuthCredential? = null,
    val isNewUser: Boolean
)
