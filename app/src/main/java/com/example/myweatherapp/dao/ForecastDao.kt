package com.example.myweatherapp.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.myweatherapp.models.WeatherModel

@Dao
interface ForecastDao {
    @Transaction
    @Query("SELECT * FROM weather where cityLat = :lat and cityLong = :longitude")
    fun getForecast(lat: Float, longitude: Float): List<WeatherModel>
}