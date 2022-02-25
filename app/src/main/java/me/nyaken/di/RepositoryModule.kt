package me.nyaken.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.nyaken.connector.Request
import me.nyaken.domain.repository.SearchRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideSearchRepository(
        connector: Request
    ): SearchRepository {
        return SearchRepository(connector)
    }

}