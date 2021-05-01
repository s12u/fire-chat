package com.tistory.mybstory.firechat.data.local.dao

import androidx.room.Dao
import androidx.room.PrimaryKey
import androidx.room.Query
import com.tistory.mybstory.firechat.domain.entity.ChatMessageEntity

@Dao
interface MessageDao {
    @Query("SELECT * FROM chat_message")
    fun getAll(): List<ChatMessageEntity>
}
