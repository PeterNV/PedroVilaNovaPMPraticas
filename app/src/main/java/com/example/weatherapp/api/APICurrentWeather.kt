package com.example.weatherapp.api

data class APICurrentWeather (
    var location : APILocation? = null,
    var current : APIWeather? = null
)