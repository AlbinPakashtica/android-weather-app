package com.example.myweatherapp.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myweatherapp.dao.ForecastDao
import com.example.myweatherapp.dao.LocationDao
import com.example.myweatherapp.dao.WeatherDao
import com.example.myweatherapp.models.LocationModel
import com.example.myweatherapp.models.WeatherModel


@Database(
    entities = [WeatherModel::class, LocationModel::class],
    version = 11,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11)]
)
abstract class RoomDB : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun locationDao(): LocationDao
    abstract fun forecastDao(): ForecastDao
}