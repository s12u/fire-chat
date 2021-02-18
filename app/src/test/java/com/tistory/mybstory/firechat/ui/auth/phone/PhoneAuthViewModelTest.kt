package com.tistory.mybstory.firechat.ui.auth.phone

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.usecase.auth.SendVerificationCodeUseCase
import com.tistory.mybstory.firechat.domain.usecase.auth.VerificationCodeSentResult
import com.tistory.mybstory.firechat.util.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class PhoneAuthViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    private lateinit var viewModel: PhoneAuthViewModel
    private lateinit var sendVerificationCodeUseCase: SendVerificationCodeUseCase

    @Before
    fun setUp() {
        sendVerificationCodeUseCase = Mockito.mock(SendVerificationCodeUseCase::class.java)
        viewModel = PhoneAuthViewModel(sendVerificationCodeUseCase)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Request sending verification code and get successful Result`() =
        testCoroutineScope.runBlockingTest {
            // given
            val mockActivity = Activity()
            viewModel.handleTextInput("+11234567890")

            // when
            Mockito.`when`(sendVerificationCodeUseCase(any()))
                .thenReturn(
                    flowOf(Result.Success(VerificationCodeSentResult("121212", any())))
                )
            viewModel.sendVerificationCode(mockActivity)

            // then
            assertThat(viewModel.phoneAuthUiStateFlow.value, equalTo(PhoneAuthUiState.Success))
            assertThat(viewModel.verificationDataFlow.value.verificationId, equalTo("121212"))
        }

    @Test
    fun `Request sending verification code and get failure Result`() =
        testCoroutineScope.runBlockingTest {
            testCoroutineScope.runBlockingTest {
                // given
                viewModel.handleTextInput("+11234567890")

                // when
                Mockito.`when`(sendVerificationCodeUseCase(any()))
                    .thenReturn(
                        flowOf(Result.Error(Exception("Failed")))
                    )
                viewModel.sendVerificationCode(Activity())

                // then
                assertThat(
                    viewModel.phoneAuthUiStateFlow.value,
                    IsInstanceOf(PhoneAuthUiState.Error::class.java)
                )
            }
        }

}
