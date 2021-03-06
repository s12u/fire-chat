package com.tistory.mybstory.firechat.domain.usecase.auth

import android.os.Parcelable
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.tistory.mybstory.firechat.di.IoDispatcher
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.parcelize.Parcelize
import javax.inject.Inject


class SignInWithCredentialUseCase @Inject constructor(
    private val auth: FirebaseAuth,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<AuthCredential, SignInWithCredentialResult>(ioDispatcher) {
    @ExperimentalCoroutinesApi
    override fun execute(parameters: AuthCredential): Flow<Result<SignInWithCredentialResult>> =
        callbackFlow {
            auth.signInWithCredential(parameters).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isNewUser = task.result?.user?.displayName.isNullOrEmpty()
                    val signInResult = SignInWithCredentialResult(isNewUser)
                    offer(Result.Success(signInResult))
                    close()
                } else {
                    offer(Result.Error(task.exception ?: Error("Sign in failed.")))
                    close()
                }
            }
            offer(Result.Loading)
            awaitClose()
        }
}

@Parcelize
data class SignInWithCredentialResult(
    val isNewUser: Boolean,
    val error: Throwable? = null
) : Parcelable
