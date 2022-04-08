package com.example.myweatherapp.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Hourly(
    @Json(name = "clouds")
    val clouds: Int = 0,
    @Json(name = "dew_point")
    val dewPoint: Double = 0.0,
    @Json(name = "dt")
    val dt: Int = 0,
    @Json(name = "feels_like")
    val feelsLike: Double = 0.0,
    @Json(name = "humidity")
    val humidity: Int = 0,
    @Json(name = "pop")
    val pop: Int = 0,
    @Json(name = "pressure")
    val pressure: Int = 0,
    @Json(name = "rain")
    val rain: Rain = Rain(),
    @Json(name = "temp")
    val temp: Double = 0.0,
    @Json(name = "uvi")
    val uvi: Double = 0.0,
    @Json(name = "visibility")
    val visibility: Int = 0,
    @Json(name = "wind_deg")
    val windDeg: Int = 0,
    @Json(name = "wind_gust")
    val windGust: Double = 0.0,
    @Json(name = "wind_speed")
    val windSpeed: Double = 0.0
)