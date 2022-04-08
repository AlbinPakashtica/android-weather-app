package com.example.myweatherapp.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.Constants
import com.example.myweatherapp.adapters.WeatherAdapter
import com.example.myweatherapp.databinding.ActivityWeatherBinding
import com.example.myweatherapp.databinding.RecycleViewHeaderWeatherBinding
import com.example.myweatherapp.models.WeatherModel
import com.example.myweatherapp.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeatherAdapter
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var headerBinding: RecycleViewHeaderWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()

    @Inject
    @Named(Constants.SHARED_PREFS)
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createBindings()
        setContentView(binding.root)
        getIntentData()
        setupRecycleView()
        observeViewModel()
    }

    private fun createBindings() {
        headerBinding = RecycleViewHeaderWeatherBinding.inflate(LayoutInflater.from(this))
        binding = ActivityWeatherBinding.inflate(layoutInflater)
    }

    private fun getIntentData() {
        val model = if (intent.hasExtra(Constants.CITY_NAME)) {
            intent.getStringExtra(Constants.CITY_NAME) ?: "No Data"
        } else {
            sharedPrefs.getString(Constants.CITY_NAME, "no city selected")!!
        }
        viewModel.setLocationModelData(model)
    }

    private fun observeViewModel() {
        viewModel.weatherItems.observe(this) {
            adapter.addAll(it as ArrayList<WeatherModel>)
        }
    }

    private fun setupRecycleView() {
        recyclerView = binding.weatherRecycleView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = WeatherAdapter()
        recyclerView.adapter = adapter
    }
}