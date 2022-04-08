package com.example.myweatherapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.myweatherapp.Constants
import com.example.myweatherapp.application.WeatherApplication
import com.example.myweatherapp.repositories.LocationModelRepository
import com.example.myweatherapp.workmanager.ApiWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val repo: LocationModelRepository) :
    ViewModel() {
    var locationItems = repo.getLocationsFromDatabase()
    val context = WeatherApplication.getInstance()
    private val workManager = WorkManager.getInstance(WeatherApplication.getInstance())
    private var work: PeriodicWorkRequest

    init {
        fetchData()

        work = PeriodicWorkRequestBuilder<ApiWorker>(15, TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 2, TimeUnit.SECONDS)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            Constants.WEATHER_WORK,
            ExistingPeriodicWorkPolicy.REPLACE,
            work
        )
    }

    private fun fetchData() {
        viewModelScope.launch {
            repo.getLocationsFromDatabase()
        }
    }
}