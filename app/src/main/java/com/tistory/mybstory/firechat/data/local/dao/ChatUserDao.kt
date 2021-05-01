package com.tistory.mybstory.firechat.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.google.firebase.firestore.auth.User
import com.tistory.mybstory.firechat.domain.entity.ChatUserEntity

@Dao
interface ChatUserDao {

    @Query("SELECT * from chat_user")
    suspend fun getAll(): List<ChatUserEntity>
}
