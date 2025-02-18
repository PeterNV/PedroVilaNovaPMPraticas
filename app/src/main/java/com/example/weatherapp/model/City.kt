package com.example.weatherapp.model

import com.google.android.gms.maps.model.LatLng

data class City(
    val name: String,
    val isMonitored: Boolean = false,
    var location: LatLng? = null,
    val salt: Long? = null, // usado para forçar atualizacao da UI
    var weather: Weather? = null,
    var forecast: List<Forecast>? = null
)