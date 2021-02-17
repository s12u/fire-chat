package com.tistory.mybstory.firechat.di

import com.tistory.mybstory.firechat.util.EventBus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideEventBus() = EventBus()
}
