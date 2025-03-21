package com.example.weatherapp.db.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.model.City
import com.google.android.gms.maps.model.LatLng

@Entity
data class LocalCity (
    @PrimaryKey
    var name: String,
    var latitude : Double,
    var longitude : Double,
    var isMonitored : Boolean
)
fun LocalCity.toCity() = City(
    name = this.name,
    location = LatLng(this.latitude, this.longitude),
    isMonitored = this.isMonitored
)
fun City.toLocalCity() = LocalCity(
    name = this.name,
    latitude = this.location?.latitude?:0.0,
    longitude = this.location?.longitude?:0.0,
    isMonitored = this.isMonitored
)