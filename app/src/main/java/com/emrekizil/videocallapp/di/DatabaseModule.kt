package com.emrekizil.videocallapp.di

import android.content.Context
import androidx.room.Room
import com.emrekizil.videocallapp.data.database.VideoCallDao
import com.emrekizil.videocallapp.data.database.VideoCallDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideVideoCallDatabase(@ApplicationContext context: Context): VideoCallDatabase {
        return Room.databaseBuilder(
            context,
            VideoCallDatabase::class.java,
            "video_call_database"
        )
            .fallbackToDestructiveMigrationFrom()
            .build()
    }

    @Provides
    fun provideVideoCallDao(videoCallDatabase: VideoCallDatabase): VideoCallDao =
        videoCallDatabase.videoCallDao()
}