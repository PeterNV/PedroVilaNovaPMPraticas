package com.example.weatherapp.api

import com.example.weatherapp.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceAPI {
    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = BuildConfig.WEATHER_API_KEY
    }
    // Procura a localização baseado no nome ou coordenadas
    @GET("current.json?key=$API_KEY&lang=pt")
    fun currentWeather(@Query("q") query: String): Call<APICurrentWeather?>
    fun search(@Query("q") query: String): Call<List<APILocation>?>
    @GET("forecast.json?key=$API_KEY&days=10&lang=pt")
    fun forecast(@Query("q") name: String): Call<APIWeatherForecast?>
}