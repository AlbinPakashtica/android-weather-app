package com.example.myweatherapp.repositories

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myweatherapp.Constants
import com.example.myweatherapp.retrofit.RetrofitClient
import com.example.myweatherapp.enums.WeatherTypes
import com.example.myweatherapp.data.RoomDB
import com.example.myweatherapp.models.LocationModel
import com.example.myweatherapp.models.WeatherModel
import com.squareup.moshi.JsonAdapter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named


@Suppress("BlockingMethodInNonBlockingContext")
class WeatherModelRepository @Inject constructor(
    @Named("json_adapter") val jsonAdapter: JsonAdapter<LocationModel>,
    @Named(Constants.SHARED_PREFS) val prefs: SharedPreferences,
    @Named("RoomDB") val db: RoomDB
) {
    private val retrofit = RetrofitClient.apiService

    private val hour = 3_600_000
    suspend fun getWeatherFromApi(
        cityName: String,
        cityLat: Double,
        cityLong: Double
    ) {
        val items = arrayListOf<WeatherModel>()
        try {
            retrofit.getWeatherExample(
                cityLat,
                cityLong,
                Constants.UNIT_OF_MEASURE,
                Constants.APP_ID
            )
                .let { response ->
                    items.add(
                        WeatherModel(
                            WeatherTypes.HEADER.ordinal,
                            cityName, cityLat, cityLong,
                            (System.currentTimeMillis() + hour),
                            response.current.weather[0].icon,
                            response.current.temp, 0.0
                        )
                    )
                    response.daily.map {
                        items.add(
                            WeatherModel(
                                WeatherTypes.WEEKDAY.ordinal,
                                SimpleDateFormat("EEEE", Locale.getDefault()).format(it.dt * 1000),
                                cityLat, cityLong, ((it.dt * 1000) + hour * 2).toLong(),
                                it.weather[0].icon, it.temp.min, it.temp.max
                            )
                        )
                    }
                }
            db.weatherDao().deleteCity(cityLat, cityLong)
            setWeatherData(cityLat, cityLong, items)

        } catch (e: Exception) {
            Log.d("customDB", "getWeatherFromApi: error on retrofit call $e")
        }
    }


    @OptIn(ExperimentalStdlibApi::class)
    suspend fun updateLastLocationWeather() {
        Log.d("customDB", "updateAllWeather: started from worker")

        val location = jsonAdapter.fromJson(prefs.getString(Constants.CITY_NAME, "") ?: "")
            ?: LocationModel("Prishtina", 1, 42.6629, 21.1655)


        Log.d("customDB", "updateAllWeather: location being fetched by worker: $location")
        getWeatherFromApi(location.locationName, location.lat, location.longitude)
    }

    private suspend fun setWeatherData(
        cityLat: Double,
        cityLong: Double,
        weatherList: List<WeatherModel>
    ) {
        db.weatherDao().updateWeatherData(cityLat, cityLong, weatherList)
    }

    fun deleteOldData() {
        db.weatherDao().deleteOld(System.currentTimeMillis())
    }

    fun getWeatherFromDatabase(cityLat: Double, cityLong: Double): LiveData<List<WeatherModel>> {
        return db.weatherDao().getWeatherForCity(cityLat, cityLong)
    }

}