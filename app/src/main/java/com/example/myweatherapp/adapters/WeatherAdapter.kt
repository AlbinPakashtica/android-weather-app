package com.example.myweatherapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myweatherapp.Constants.ICON_URL
import com.example.myweatherapp.R
import com.example.myweatherapp.application.WeatherApplication
import com.example.myweatherapp.enums.WeatherTypes
import com.example.myweatherapp.databinding.RecycleViewDayBinding
import com.example.myweatherapp.databinding.RecycleViewGraphBinding
import com.example.myweatherapp.databinding.RecycleViewHeaderWeatherBinding
import com.example.myweatherapp.models.WeatherModel


class WeatherAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var weatherItems: ArrayList<WeatherModel> = arrayListOf()


    @SuppressLint("NotifyDataSetChanged")
    fun addAll(itemsList: ArrayList<WeatherModel>) {
        weatherItems.clear()
        if (itemsList.isNotEmpty() && itemsList.size > 3) {
            for (item in itemsList)
                weatherItems.add(item)
            weatherItems.add(WeatherModel(type = WeatherTypes.GRAPH.type))
        }
        notifyDataSetChanged()
        Log.d("customDB", "adapter list, inside addAll func: $weatherItems")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            WeatherTypes.HEADER.type -> {
                val headerBinding = RecycleViewHeaderWeatherBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(headerBinding.root)
            }
            WeatherTypes.WEEKDAY.type -> {
                val dayBinding =
                    RecycleViewDayBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                DayViewHolder(dayBinding.root)
            }
            else -> {
                val graphBinding = RecycleViewGraphBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GraphViewHolder(graphBinding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.setupView(weatherItems[position])
            is DayViewHolder -> holder.setupView(weatherItems[position])
            is GraphViewHolder -> holder.setupView()
        }
    }

    override fun getItemCount(): Int {
        return weatherItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return weatherItems[position].type
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = RecycleViewHeaderWeatherBinding.bind(itemView)

        private val tvName: TextView = binding.weatherCityName
        private val tvImg: ImageView = binding.weatherCityIcon
        private val tvMin: TextView = binding.minTemp

        @SuppressLint("SetTextI18n")
        fun setupView(weatherModel: WeatherModel) {
            tvName.text = weatherModel.name
            if (weatherModel.dateTime < System.currentTimeMillis()) {
                tvMin.text =
                    "${WeatherApplication.getInstance().resources.getString(R.string.current_temp)}: -- °C"
            } else {
                tvMin.text =
                    "${WeatherApplication.getInstance().resources.getString(R.string.current_temp)}: ${weatherModel.min}°C"
            }
            tvImg.load("${ICON_URL + weatherModel.img}@4x.png")
            Log.d("imageview", "setupView: " + ("${ICON_URL + weatherModel.img}@4x.png"))
        }

    }

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = RecycleViewDayBinding.bind(itemView)

        private val tvName: TextView = binding.weatherDayName
        private val tvImg: ImageView = binding.weatherDayIcon
        private val tvMin: TextView = binding.minTemp
        private val tvMax: TextView = binding.maxTemp

        @SuppressLint("SetTextI18n")
        fun setupView(weatherModel: WeatherModel) {
            tvName.text = weatherModel.name
            tvImg.load("${ICON_URL + weatherModel.img}@4x.png")
            tvMin.text = "Min: ${weatherModel.min}°C"
            tvMax.text = "Max: ${weatherModel.max}°C"
        }
    }

    inner class GraphViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RecycleViewGraphBinding.bind(itemView)

        fun setupView() {
            try {
                Log.d("customDB", "setupView: weatheritems= $weatherItems ")
                binding.myGraphView.setData(weatherItems)
            } catch (E: Exception) {
                Log.d("customDB", "setupView: exception $E")
            }
        }
    }

}