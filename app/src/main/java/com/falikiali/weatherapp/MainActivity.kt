package com.falikiali.weatherapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.falikiali.weatherapp.databinding.ActivityMainBinding
import com.falikiali.weatherapp.helper.ResultState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_CODE_LOCATION = 900
    }

    private val fusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this@MainActivity) }
    private val locationManager: LocationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val weatherCityAdapter: WeatherCityAdapter by lazy { WeatherCityAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRv()
        observeViewModel()
        getCurrentLocationWeather()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationWeather()
            } else {
                val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                alertDialogBuilder.setMessage("This app requires permission that have denied. Please allow location permission from app settings to proceed further.")
                    .setTitle("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton("Go to settings") { dialog, _ ->
                        val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
                        startActivity(i)
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun initRv() {
        binding.rvWeatherCity.apply {
            adapter = weatherCityAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            resultDb.observe(this@MainActivity) {
                if (it.isNotEmpty()) {
                    binding.apply {
                        tvLocation.text = it[0].city
                        tvDate.text = "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                            Date(it[0].date * 1000)
                        )
                        tvWeather.text = it[0].weather
                        tvTemp.text = "${it[0].temp}°C"
                        tvMaxTemp.text = "Max temp: ${it[0].maxTemp}°C"
                        tvMinTemp.text = "Min temp: ${it[0].minTemp}°C"
                        tvWind.text = it[0].wind.toString()
                        tvHumidity.text = it[0].humidity.toString()
                        tvPressure.text = it[0].pressure.toString()
                    }
                }
            }

            result.observe(this@MainActivity) {
                when (it) {
                    is ResultState.Loading -> binding.tvDate.text = "Updating"
                    is ResultState.Failed -> Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_SHORT).show()
                    else -> getCurrentLocationWeatherFromDB()
                }
            }

            resultByCity.observe(this@MainActivity) {
                when (it) {
                    is ResultState.Loading -> {}
                    is ResultState.Failed -> {
                        binding.cvWeatherCity.isVisible = false
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Success -> {
                        binding.cvWeatherCity.isVisible = true
                        weatherCityAdapter.submitList(it.data)
                        Log.d("LIST CITY", it.data.toString())
                    }
                }
            }
        }
    }

    private fun getCurrentLocationWeather() {
        if (checkPermission()) {
            if (isGPSEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location: Location? = it.result
                    if (location == null) {
                        Toast.makeText(this@MainActivity, "Location unkown. Please try again", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.getCurrentLocationWeather(location.latitude, location.longitude)
                        viewModel.getCurrentWeatherByCity()
                    }
                }
            } else {
                val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                alertDialogBuilder.setMessage("This app requires to access your location. Please turn on your location.")
                    .setTitle("Access Location Required")
                    .setCancelable(false)
                    .setPositiveButton("Go to settings") { dialog, _ ->
                        val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(i)
                        dialog.dismiss()
                    }
                    .show()
            }
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_CODE_LOCATION
        )
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        return false
    }

    private fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

}