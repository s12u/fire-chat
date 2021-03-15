package com.tistory.mybstory.firechat.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message")
data class ChatMessageEntity (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "value")
    val value: String
)
