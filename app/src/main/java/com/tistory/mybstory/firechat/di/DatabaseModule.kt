package com.tistory.mybstory.firechat.di

import android.content.Context
import androidx.room.Room
import com.tistory.mybstory.firechat.data.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            LocalDatabase::class.java,
            "local_db"
        )
}
