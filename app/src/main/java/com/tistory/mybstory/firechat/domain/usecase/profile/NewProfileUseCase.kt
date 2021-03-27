package com.tistory.mybstory.firechat.domain.usecase.profile

//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
//import com.tistory.mybstory.firechat.di.IoDispatcher
//import com.tistory.mybstory.firechat.domain.Result
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import javax.inject.Inject
//
//class NewProfileUseCase @Inject constructor(
//    private val auth: FirebaseAuth,
//    private val firestore: FirebaseFirestore,
//    private val firebaseStorage: FirebaseStorage,
//    @IoDispatcher ioDispatcher: CoroutineDispatcher
//): UseCase<UserProfile, Nothing>(ioDispatcher) {
//
//    @ExperimentalCoroutinesApi
//    override fun execute(parameters: UserProfile): Flow<Result<Nothing>> = callbackFlow {
//
//        TODO("Not yet implemented")
//    }
//}
//
