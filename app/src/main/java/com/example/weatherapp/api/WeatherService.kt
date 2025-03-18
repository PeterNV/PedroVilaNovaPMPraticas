package com.example.weatherapp.api

import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.HttpException

class WeatherService {
    private var weatherAPI: WeatherServiceAPI

    init {
        val retrofitAPI = Retrofit.Builder()
            .baseUrl(WeatherServiceAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        weatherAPI = retrofitAPI.create(WeatherServiceAPI::class.java)
    }

    // 🔹 Obtém o nome da localização a partir da latitude e longitude
    suspend fun getName(lat: Double, lng: Double): String? = withContext(Dispatchers.IO) {
        search("$lat,$lng")?.name
    }

    // 🔹 Obtém a localização (LatLng) de um nome de cidade
    suspend fun getLocation(name: String): LatLng? = withContext(Dispatchers.IO) {
        search(name)?.let { loc ->
            loc.lat?.let { lat -> loc.lon?.let { lon -> LatLng(lat, lon) } }
        }
    }

    // 🔹 Busca informações de localização na API
    private suspend fun search(query: String): APILocation? = withContext(Dispatchers.IO) {
        try {
            val response = weatherAPI.search(query).execute().body()
            response?.firstOrNull() // Retorna o primeiro resultado se houver
        } catch (e: HttpException) {
            Log.w("WeatherApp WARNING", "Erro ao buscar localização: ${e.message}")
            null
        }
    }

    // 🔹 Obtém a previsão do tempo atual
    suspend fun getCurrentWeather(name: String): APICurrentWeather? = withContext(Dispatchers.IO) {
        try {
            weatherAPI.currentWeather(name).execute().body()
        } catch (e: HttpException) {
            Log.w("WeatherApp WARNING", "Erro ao buscar clima atual: ${e.message}")
            null
        }
    }

    // 🔹 Obtém a previsão do tempo para os próximos dias
    suspend fun getForecast(name: String): APIWeatherForecast? = withContext(Dispatchers.IO) {
        try {
            weatherAPI.forecast(name).execute().body()
        } catch (e: HttpException) {
            Log.w("WeatherApp WARNING", "Erro ao buscar previsão do tempo: ${e.message}")
            null
        }
    }

    // 🔹 Obtém um bitmap da imagem do tempo
    suspend fun getBitmap(imgUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            Picasso.get().load(imgUrl).get()
        } catch (e: Exception) {
            Log.w("WeatherApp WARNING", "Erro ao carregar imagem: ${e.message}")
            null
        }
    }
}
