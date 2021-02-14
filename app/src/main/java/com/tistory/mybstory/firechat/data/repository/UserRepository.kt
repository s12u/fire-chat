package com.tistory.mybstory.firechat.data.repository

import com.tistory.mybstory.firechat.base.model.ChatUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun get(id: String): Flow<ChatUser>
}
