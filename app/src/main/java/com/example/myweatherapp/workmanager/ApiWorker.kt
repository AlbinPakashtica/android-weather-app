package com.example.myweatherapp.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myweatherapp.repositories.WeatherModelRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

const val TAG = "ApiWorker"


class ApiWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    val weatherRepository: WeatherModelRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "doWork: ApiWorker started")
            weatherRepository.updateLastLocationWeather()
            Log.d(TAG, "doWork: ApiWorker finished")
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "doWork: ApiWorker encountered error $e")
            Result.retry()
        }
    }
}