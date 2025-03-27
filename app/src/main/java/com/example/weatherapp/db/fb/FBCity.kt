package com.example.weatherapp.db.fb

import com.example.weatherapp.model.City
import com.google.android.gms.maps.model.LatLng

class FBCity {
    var name : String? = null
    var lat : Double? = null
    var lng : Double? = null
    var  monitored: Boolean = false
    fun toCity(): City {
        val latlng = if (lat!=null&&lng!=null) LatLng(lat!!, lng!!) else null
        return City(name!!, weather = null, location = latlng, isMonitored = monitored)
    }
}

fun City.toFBCity() : FBCity {
    val fbCity = FBCity()
    fbCity.monitored = this.isMonitored
    fbCity.name = this.name
    fbCity.lat = this.location?.latitude ?: 0.0
    fbCity.lng = this.location?.longitude ?: 0.0

    return fbCity
}