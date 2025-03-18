package com.example.weatherapp.db.local

import android.content.Context
import androidx.room.Room

import com.example.weatherapp.model.City
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map

import kotlinx.coroutines.launch

class LocalDatabase(context: Context, databaseName: String) {
    private var roomDB: LocalRoomDatabase = Room.databaseBuilder(
        context = context,
        klass = LocalRoomDatabase::class.java,
        name = databaseName
    ).build()
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun insert(city: LocalCity) = scope.launch {
        roomDB.localCityDao().upsert(city)
    }

    fun update(city: LocalCity) = scope.launch {
        roomDB.localCityDao().upsert(city)
    }

    fun delete(city: LocalCity) = scope.launch {
        roomDB.localCityDao().delete(city)
    }

    // Renomeado para evitar conflito
    //fun getCitiesAsDomain() = roomDB.localCityDao().getCities().map { list -> list.map { it.toCity() } }

    // Mantém o fluxo original de LocalCity
    val cities = roomDB.localCityDao().getCities()
}
