package com.example.myweatherapp.repositories

import androidx.lifecycle.LiveData
import com.example.myweatherapp.data.RoomDB
import com.example.myweatherapp.models.LocationModel
import javax.inject.Inject
import javax.inject.Named

class LocationModelRepository @Inject constructor(@Named("RoomDB") val db: RoomDB) {

    fun getLocationsFromDatabase(): LiveData<List<LocationModel>> {
        return db.locationDao().getLocations()
    }
}