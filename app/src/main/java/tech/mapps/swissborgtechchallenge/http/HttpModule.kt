package tech.mapps.swissborgtechchallenge.http

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HttpModule {
    @Provides
    fun httpClient() = httpClient
}