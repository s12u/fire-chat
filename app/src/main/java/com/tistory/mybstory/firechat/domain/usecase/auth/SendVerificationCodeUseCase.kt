package com.tistory.mybstory.firechat.domain.usecase.auth

import android.app.Activity
import android.os.Parcelable
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.tistory.mybstory.firechat.di.IoDispatcher
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SendVerificationCodeUseCase @Inject constructor(
    private val auth: FirebaseAuth,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<Pair<Activity, String>, VerificationCodeSentResult>(ioDispatcher) {
    @ExperimentalCoroutinesApi
    override fun execute(parameters: Pair<Activity, String>): Flow<Result<VerificationCodeSentResult>> {
        return callbackFlow {
            Timber.e("given: $parameters")
            val (activity, givenPhoneNumber) = parameters
            val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    offer(
                        Result.Success(
                            VerificationCodeSentResult(
                                verificationId,
                                token,
                                null,
                                false
                            )
                        )
                    )
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    offer(Result.Error(e))
                    close()
                }

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Timber.e("auto sign in ?")
                    // TODO: exception on real phone sms. :(
                    offer(
                        Result.Success(
                            VerificationCodeSentResult(
                                "",
                                authCredential = credential,
                                autoSignIn = true
                            )
                        )
                    )
                    close()
                }
            }
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(givenPhoneNumber)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callback)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
            offer(Result.Loading)
            awaitClose {

            }
        }
    }
}

@Parcelize
data class VerificationCodeSentResult(
    val verificationId: String,
    val resendingToken: PhoneAuthProvider.ForceResendingToken? = null,
    val authCredential: PhoneAuthCredential? = null,
    val autoSignIn: Boolean,
) : Parcelable
