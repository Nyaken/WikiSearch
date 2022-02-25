package me.nyaken.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.nyaken.connector.Request
import me.nyaken.httpconnection.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideClient(
    ): Request {
        return Request.Builder()
            .url(BuildConfig.BASE_URL)
            .connectTimeout(3000)
            .readTimeOut(3000)
            .addHeader("Content-Type", "application/json")
            .build()
    }

}