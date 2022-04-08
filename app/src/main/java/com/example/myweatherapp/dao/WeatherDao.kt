package com.example.myweatherapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myweatherapp.models.WeatherModel

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg weather: WeatherModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weather: List<WeatherModel>)

    @Delete
    suspend fun deleteFromDb(weather: WeatherModel)

    @Query("DELETE FROM weather where type = 1 and dateTime < :dateTime")
    fun deleteOld(dateTime: Long)

    @Query("DELETE FROM weather WHERE cityLat = :lat and cityLong = :longitude")
    suspend fun deleteCity(lat: Double, longitude: Double)

    @Query("SELECT * FROM weather where cityLat = :lat and cityLong = :longitude")
    fun getWeatherForCity(lat: Double, longitude: Double): LiveData<List<WeatherModel>>

    @Transaction
    suspend fun updateWeatherData(lat: Double, longitude: Double, weather: List<WeatherModel>) {
        deleteCity(lat, longitude)
        insertAll(weather)
    }
}