package com.example.myweatherapp.models

import androidx.room.Embedded
import androidx.room.Relation

data class Forecast(
    @Embedded val locationModel: LocationModel,
    @Relation(
        parentColumn = "lId",
        entityColumn = "city"
    )
    val weatherModel: List<WeatherModel>
)
