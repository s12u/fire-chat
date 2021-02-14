package com.tistory.mybstory.firechat.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tistory.mybstory.firechat.data.repository.ChatRepository
import com.tistory.mybstory.firechat.data.repository.impl.ChatRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

//    @Provides
//    @Singleton
//    fun provideUserRepository():

    @Provides
    @Singleton
    fun provideChatRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): ChatRepository = ChatRepositoryImpl(firebaseAuth, firestore)
}
