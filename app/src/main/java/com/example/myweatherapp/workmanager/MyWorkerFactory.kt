package com.example.myweatherapp.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.myweatherapp.repositories.WeatherModelRepository

class MyWorkerFactory(private val repository: WeatherModelRepository) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return ApiWorker(appContext, workerParameters, repository)
    }
}