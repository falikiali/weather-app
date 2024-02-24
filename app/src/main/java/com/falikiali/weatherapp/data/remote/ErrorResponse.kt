package com.falikiali.weatherapp.data.remote

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("cod") val code: Int,
    @SerializedName("message") val message: String
)
