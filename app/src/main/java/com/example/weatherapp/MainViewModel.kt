package com.example.weatherapp

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.api.toForecast
import com.example.weatherapp.api.toWeather
import com.example.weatherapp.db.fb.FBDatabase
import com.example.weatherapp.model.City
import com.example.weatherapp.model.User
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.monitor.ForecastMonitor
import com.example.weatherapp.repo.Repository
import com.example.weatherapp.ui.nav.Route
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(
    private val repository: Repository,
    private val service: WeatherService,
    private val monitor: ForecastMonitor
) : ViewModel() {

    private var _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }

    private var _city = mutableStateOf<City?>(null)
    var city: City?
        get() = _city.value
        set(tmp) { _city.value = tmp?.copy(salt = Random.nextLong()) }

    private val _cities = mutableStateMapOf<String, City>()
    val cities: List<City>
        get() = _cities.values.toList()

    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    init {
        viewModelScope.launch(Dispatchers.Main) {
            repository.user.collect { user ->
                _user.value = user.copy()
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            repository.cities.collect { list ->
                val names = list.map { it.name }
                val newCities = list.filter { it.name !in _cities.keys }
                val oldCities = list.filter { it.name in _cities.keys }

                _cities.keys.removeIf { it !in names }
                newCities.forEach { _cities[it.name] = it }
                oldCities.forEach { refresh(it) }
            }
        }
    }

    fun add(name: String) = viewModelScope.launch(Dispatchers.IO) {
        val location = service.getLocation(name) ?: return@launch
        repository.add(City(name = name, location = location))
    }

    fun add(location: LatLng) = viewModelScope.launch(Dispatchers.IO) {
        val name = service.getName(location.latitude, location.longitude) ?: return@launch
        repository.add(City(name = name, location = location))
    }

    fun update(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(city)
        refresh(city)
    }

    fun remove(city: City) = viewModelScope.launch(Dispatchers.IO) {
        repository.remove(city)
    }

    private fun refresh(city: City) {
        val copy = city.copy(
            salt = Random.nextLong(),
            weather = city.weather ?: _cities[city.name]?.weather,
            forecast = city.forecast ?: _cities[city.name]?.forecast
        )
        _cities[city.name] = copy
        monitor.updateCity(copy)
    }

    fun loadWeather(city: City) = viewModelScope.launch(Dispatchers.IO) {
        city.weather = service.getCurrentWeather(city.name)?.toWeather()
        refresh(city)
    }

    fun loadForecast(city: City) = viewModelScope.launch(Dispatchers.IO) {
        city.forecast = service.getForecast(city.name)?.toForecast()
        refresh(city)
    }

    fun loadBitmap(city: City) = viewModelScope.launch(Dispatchers.IO) {
        city.weather?.bitmap = service.getBitmap(city.weather?.imgUrl ?: return@launch)
        refresh(city)
    }
}

class MainViewModelFactory(
    private val repository: Repository,
    private val service: WeatherService,
    private val monitor: ForecastMonitor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository, service, monitor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

