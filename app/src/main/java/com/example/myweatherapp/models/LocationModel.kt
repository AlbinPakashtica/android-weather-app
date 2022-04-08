package com.example.myweatherapp.models

import androidx.room.Entity

@Entity(tableName = "locations", primaryKeys = ["lat", "longitude"])
data class LocationModel(
    val locationName: String = "default",
    val locationType: Int = 0,
    val lat: Double = 0.0,
    val longitude: Double = 0.0,
)