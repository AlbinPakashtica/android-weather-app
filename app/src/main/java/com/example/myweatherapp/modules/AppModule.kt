package com.example.myweatherapp.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.myweatherapp.Constants
import com.example.myweatherapp.data.RoomDB
import com.example.myweatherapp.models.LocationModel
import com.example.myweatherapp.repositories.WeatherModelRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named(Constants.SHARED_PREFS)
    fun provideSharedPrefsInstance(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    @Named("json_adapter")
    fun provideJsonAdapter(): JsonAdapter<LocationModel> {
        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(LocationModel::class.java)
    }

    @Singleton
    @Provides
    @Named("weather_repo")
    fun provideWeatherRepository(
        @Named("json_adapter") jsonAdapter: JsonAdapter<LocationModel>,
        @Named(Constants.SHARED_PREFS) prefs: SharedPreferences,
        @Named("RoomDB") db: RoomDB
    ) = WeatherModelRepository(jsonAdapter, prefs, db)

    @Singleton
    @Provides
    @Named("RoomDB")
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RoomDB::class.java, "weather_db")
            .createFromAsset("database/weather_db.db")
            .build()

}