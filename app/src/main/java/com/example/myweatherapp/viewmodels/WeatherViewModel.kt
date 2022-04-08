package com.example.myweatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.Constants.CITY_NAME
import com.example.myweatherapp.models.LocationModel
import com.example.myweatherapp.models.WeatherModel
import com.example.myweatherapp.repositories.WeatherModelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repo: WeatherModelRepository
) : ViewModel() {

    lateinit var locationModel: String
    lateinit var weatherItems: LiveData<List<WeatherModel>>
    var location: LocationModel? = null


    fun setLocationModelData(model: String) {
        locationModel = model
        location = repo.jsonAdapter.nonNull().fromJson(locationModel)
        if (location != null) {
            weatherItems = repo.getWeatherFromDatabase(location!!.lat, location!!.longitude)
            viewModelScope.launch(Dispatchers.IO) { repo.deleteOldData() }
            savePreferenceLocation()
            fetchData(location!!)
        }
    }

    private fun savePreferenceLocation() {
        repo.prefs.edit().putString(CITY_NAME, locationModel).apply()
    }

    private fun fetchData(location: LocationModel) {

        val cityName = location.locationName
        val cityLat = location.lat
        val cityLong = location.longitude
        Log.d("customDB", "fetchData: location going to worker is $cityName")

        viewModelScope.launch {
            repo.getWeatherFromApi(cityName, cityLat, cityLong)
        }
    }
}