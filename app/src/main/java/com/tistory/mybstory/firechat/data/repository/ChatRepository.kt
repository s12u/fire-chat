package com.tistory.mybstory.firechat.data.repository

import com.google.firebase.Timestamp
import com.tistory.mybstory.firechat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getByBefore(chatId: String, lastTime: Timestamp): Flow<List<ChatMessage>>
}
