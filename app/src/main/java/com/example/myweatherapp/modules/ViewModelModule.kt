package com.example.myweatherapp.modules

import com.example.myweatherapp.data.RoomDB
import com.example.myweatherapp.repositories.LocationModelRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @Named("location_repo")
    fun provideLocationRepo(@Named("RoomDB") db: RoomDB) = LocationModelRepository(db)


}