package com.tistory.mybstory.firechat.data.repository.impl

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tistory.mybstory.firechat.domain.model.ChatMessage
import com.tistory.mybstory.firechat.data.repository.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatRepositoryImpl @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore
) : ChatRepository {
    override fun getByBefore(chatId: String, lastTime: Timestamp): Flow<List<ChatMessage>> {
        val collectionRefFlow = flow {
            emit(
                firestore.collection("chat")
                    .document(chatId)
                    .collection("msg")
            )
        }
        val snapshotFlow = collectionRefFlow.flatMapLatest {
            it.whereLessThan("timestamp", lastTime)
                .limit(15).toFlow()
        }
        return snapshotFlow.map { it.toObjects(ChatMessage::class.java) }
    }
}

@ExperimentalCoroutinesApi
private fun Query.toFlow() = callbackFlow {
    val listener = addSnapshotListener { snapshot, exception ->
        if (exception != null) close(exception)
        if (snapshot != null) {
            offer(snapshot)
        }
    }
    awaitClose { listener.remove() }
}
