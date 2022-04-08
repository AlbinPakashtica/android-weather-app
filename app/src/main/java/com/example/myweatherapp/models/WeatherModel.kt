package com.example.myweatherapp.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "weather",
    foreignKeys = [ForeignKey(
        entity = LocationModel::class,
        parentColumns = arrayOf("lat", "longitude"),
        childColumns = arrayOf("cityLat", "cityLong"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class WeatherModel(

    val type: Int = 1,
    val name: String = "Default",
    val cityLat: Double = 0.0,
    val cityLong: Double = 0.0,
    val dateTime: Long = 1648542720000,
    val img: String? = "0d0",
    val min: Double = 0.0,
    val max: Double = 20.0
) {
    @PrimaryKey(autoGenerate = true)
    var wId: Int = 0
}