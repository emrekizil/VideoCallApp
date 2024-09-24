package com.emrekizil.videocallapp.di

import com.emrekizil.videocallapp.BuildConfig
import com.emrekizil.videocallapp.data.api.AuthInterceptor
import com.emrekizil.videocallapp.data.api.VideoCallApi
import com.emrekizil.videocallapp.media.ChatTokenBuilder2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {

    @Provides
    @ViewModelScoped
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor {
            ChatTokenBuilder2().buildAppToken(
                BuildConfig.appId, BuildConfig.certificate, 600
            )
        }
    }

    @Provides
    @ViewModelScoped
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://a41.chat.agora.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideVideoCallApi(retrofit: Retrofit): VideoCallApi {
        return retrofit.create(VideoCallApi::class.java)
    }

}