package com.example.myweatherapp.retrofit

import com.example.myweatherapp.models.Example
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("onecall")
    suspend fun getWeatherExample(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Example
}