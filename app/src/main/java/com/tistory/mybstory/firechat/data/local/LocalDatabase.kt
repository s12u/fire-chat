package com.tistory.mybstory.firechat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tistory.mybstory.firechat.data.local.dao.ChatUserDao
import com.tistory.mybstory.firechat.data.local.dao.MessageDao
import com.tistory.mybstory.firechat.domain.entity.ChatMessageEntity
import com.tistory.mybstory.firechat.domain.entity.ChatUserEntity

@Database(entities = [ChatUserEntity::class, ChatMessageEntity::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun userDao(): ChatUserDao
    abstract fun messageDao(): MessageDao
}
