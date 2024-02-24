package com.falikiali.weatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.falikiali.weatherapp.databinding.ListItemWeatherCityBinding
import com.falikiali.weatherapp.domain.model.Weather

class WeatherCityAdapter: ListAdapter<Weather, WeatherCityAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Weather>() {
            override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
                return oldItem.city == newItem.city
            }

            override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ViewHolder(private val binding: ListItemWeatherCityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Weather) {
            binding.apply {
                tvCity.text = data.city
                tvTemp.text = data.temp.toString() + "°C"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherCityAdapter.ViewHolder {
        val binding = ListItemWeatherCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherCityAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}