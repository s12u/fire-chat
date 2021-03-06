package com.tistory.mybstory.firechat.data.repository.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tistory.mybstory.firechat.data.repository.UserRepository
import com.tistory.mybstory.firechat.domain.model.ChatUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore
) : UserRepository {
    override fun get(id: String): Flow<ChatUser> {
        TODO("Not yet implemented")
    }
}

fun FirebaseAuth.getCurrentUserId(): String? = currentUser?.uid
