package com.emrekizil.videocallapp.di

import android.content.Context
import com.emrekizil.videocallapp.data.repository.AgoraRepository
import com.emrekizil.videocallapp.data.repository.VideoCallRepository
import com.emrekizil.videocallapp.data.repository.VideoCallRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindVideoCallRepository(videoCallRepositoryImpl: VideoCallRepositoryImpl): VideoCallRepository
}

@Module
@InstallIn(ViewModelComponent::class)
object AgoraRepositoryModule{
    @Provides
    @ViewModelScoped
    fun provideAgoraRepository(@ApplicationContext context: Context) :AgoraRepository{
        return AgoraRepository(context)
    }
}