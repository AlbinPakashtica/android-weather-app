package com.example.myweatherapp.adapters

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.enums.LocationTypes
import com.example.myweatherapp.R
import com.example.myweatherapp.application.WeatherApplication
import com.example.myweatherapp.databinding.RecycleViewCityBinding
import com.example.myweatherapp.databinding.RecycleViewCountryBinding
import com.example.myweatherapp.models.LocationModel


class CustomAdapter(private val cityListener: CityListener) :
    RecyclerView.Adapter<CustomAdapter.BaseViewHolder>() {

    private var items = arrayListOf<LocationModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: ArrayList<LocationModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == LocationTypes.COUNTRY.viewType) {
            val countryBinding = RecycleViewCountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            CountryViewHolder(countryBinding.root)
        } else {
            val cityBinding =
                RecycleViewCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CityViewHolder(cityBinding.root, cityListener)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.setupView(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].locationType
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        abstract var textView: TextView

        abstract fun setupView(location: LocationModel)

    }

    class CountryViewHolder(view: View) : BaseViewHolder(view) {

        override var textView: TextView = RecycleViewCountryBinding.bind(view).countryName
        override fun setupView(location: LocationModel) {
            textView.text = location.locationName
        }
    }

    class CityViewHolder(view: View, private val cityListener: CityListener) :
        BaseViewHolder(view) {

        override var textView: TextView = RecycleViewCityBinding.bind(view).cityName
        private val clicked =
            WeatherApplication.getInstance().getString(R.string.toast_clicked_city)

        override fun setupView(location: LocationModel) {
            textView.text = location.locationName
            if (location.locationName.startsWith("P")) {
                Log.d("locationName", "setupView: ${location.locationName}")
                textView.typeface = Typeface.DEFAULT_BOLD
            } else {
                textView.typeface = Typeface.DEFAULT
            }
            textView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "$clicked ${location.locationName}",
                    Toast.LENGTH_SHORT
                ).show()
                cityListener.onCityClicked(location)
            }
        }
    }

    interface CityListener {
        fun onCityClicked(location: LocationModel)
    }
}