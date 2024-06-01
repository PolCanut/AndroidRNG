package edu.uoc.rng2.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.uoc.rng2.repositories.datasources.GamesResultServiceDataSource
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkDataSourcesProvider {
    @Singleton
    @Provides
    fun provideGamesResultServiceDataSource(): GamesResultServiceDataSource {
        return Retrofit.Builder()
            .baseUrl("https://rnguoc-default-rtdb.europe-west1.firebasedatabase.app/")
            .addConverterFactory(Json { explicitNulls = false }.asConverterFactory(MediaType.get("application/json; charset=UTF8")))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(GamesResultServiceDataSource::class.java)
    }
}