package com.example.myweatherapp.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Example(
    @Json(name = "current")
    val current: Current = Current(),
    @Json(name = "daily")
    val daily: List<Daily> = listOf(),
    @Json(name = "lat")
    val lat: Double = 0.0,
    @Json(name = "lon")
    val lon: Double = 0.0,
    @Json(name = "timezone")
    val timezone: String = "",
    @Json(name = "timezone_offset")
    val timezoneOffset: Int = 0
)