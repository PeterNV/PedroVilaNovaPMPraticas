package com.example.weatherapp

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.db.fb.FBDatabase
import com.example.weatherapp.model.City
import com.example.weatherapp.model.User
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.ui.nav.Route
import com.google.android.gms.maps.model.LatLng

private fun getCities() = List(20) { i ->
    City(name = "Cidade $i")
}
/*
class MainViewModel : ViewModel() {
    private val _user = mutableStateOf<User?> (null)
    val user : User?
        get() = _user.value
    private val _cities = getCities().toMutableStateList()
    val cities
        get() = _cities.toList()
    fun remove(city: City) {
        _cities.remove(city)
    }
    fun add(name: String, location: LatLng? = null) {
        _cities.add(City(name = name, location = location))
    }
}
*/
class MainViewModel (private val db: FBDatabase, private val service : WeatherService): ViewModel(),
    FBDatabase.Listener {
    private var _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }

    private var _city = mutableStateOf<City?>(null)
    var city: City?
        get() = _city.value
        set(tmp) { _city = mutableStateOf(tmp?.copy()) }

    private val _cities = mutableStateMapOf<String, City>()
    val cities : List<City>
        get() = _cities.values.toList()
    /*
    private val _cities = mutableStateListOf<City>()
    val cities
        get() = _cities.toList()
    */
    private val _user = mutableStateOf<User?> (null)
    val user : User?
        get() = _user.value

    init {
        db.setListener(this)
    }

    fun remove(city: City) {
        db.remove(city)
    }
    /*
    fun add(name: String, location : LatLng? = null) {
        db.add(City(name = name, location = location))
    }
    */
    fun add(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(City(name = name, location = LatLng(lat, lng)))
            }
        }
    }
    fun add(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(City(name = name, location = location))
            }
        }
    }

    override fun onCityUpdated(city: City) {
        _cities.remove(city.name)
        _cities[city.name] = city.copy()
        if (_city.value?.name == city.name) {
            _city.value = city.copy()
        }
    }

    override fun onUserLoaded(user: User) {
        _user.value = user
    }

    override fun onCityAdded(city: City) { _cities[city.name] = city }

    override fun onCityUpdate(city: City) {
        _cities.remove(city.name)
        _cities[city.name] = city.copy()
    }

    override fun onCityRemoved(city: City) {
        _cities.remove(city.name)
    }

        fun loadWeather(city: City) {
        service.getCurrentWeather(city.name) { apiWeather ->
            city.weather = Weather (
                date = apiWeather?.current?.last_updated?:"...",
                desc = apiWeather?.current?.condition?.text?:"...",
                temp = apiWeather?.current?.temp_c?:-1.0,
                imgUrl = "https:" + apiWeather?.current?.condition?.icon
            )
            _cities.remove(city.name)
            _cities[city.name] = city.copy()
        }
    }

    fun loadForecast(city : City) {
        service.getForecast(city.name) { result ->
            city.forecast = result?.forecast?.forecastday?.map {
                Forecast(
                    date = it.date?:"00-00-0000",
                    weather = it.day?.condition?.text?:"Erro carregando!",
                    tempMin = it.day?.mintemp_c?:-1.0,
                    tempMax = it.day?.maxtemp_c?:-1.0,
                    imgUrl = ("https:" + it.day?.condition?.icon)
                )
            }
            _cities.remove(city.name)
            _cities[city.name] = city.copy()
        }
    }
}
class MainViewModelFactory(private val db : FBDatabase,
                           private val service : WeatherService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db, service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}