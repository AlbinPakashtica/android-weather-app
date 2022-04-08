package com.example.myweatherapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myweatherapp.models.LocationModel

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg location: LocationModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(location: List<LocationModel>)

    @Delete
    suspend fun delete(location: LocationModel)

    @Query("DELETE FROM locations WHERE lat = :lat and longitude = :longitude")
    suspend fun deleteCity(lat: Float, longitude: Float)

    @Query("SELECT * FROM locations where lat = :lat and longitude = :longitude")
    suspend fun get(lat: Float, longitude: Float): List<LocationModel>

    @Query("SELECT * FROM locations")
    fun getAll(): LiveData<List<LocationModel>>

    @Query("SELECT * FROM locations")
    fun getLocations(): List<LocationModel>

}