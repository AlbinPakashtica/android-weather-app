package com.example.myweatherapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.Constants.CITY_NAME
import com.example.myweatherapp.adapters.CustomAdapter
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.models.LocationModel
import com.example.myweatherapp.viewmodels.LocationViewModel
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CustomAdapter.CityListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: LocationViewModel by viewModels()

    @Inject
    @Named("json_adapter")
    lateinit var jsonAdapter: JsonAdapter<LocationModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecycleView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.locationItems.observe(this) {
            adapter.setItems(it as ArrayList<LocationModel>)
        }
    }

    private fun setupRecycleView() {
        //initializing the recyclerView and giving it our custom adapter
        recyclerView = binding.locationRecycler
        adapter = CustomAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCityClicked(location: LocationModel) {
        //click listener implementation that sends location data to the second activity
        val model = jsonAdapter.toJson(location)
        Intent(this, WeatherActivity::class.java).apply {
            putExtra(CITY_NAME, model)
            startActivity(this)
        }
    }
}