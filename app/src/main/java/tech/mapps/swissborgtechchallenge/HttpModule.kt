package tech.mapps.swissborgtechchallenge

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tech.mapps.swissborgtechchallenge.http.httpClient

@Module
@InstallIn(SingletonComponent::class)
class HttpModule {
    @Provides
    fun httpClient() = httpClient
}