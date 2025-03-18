package com.example.weatherapp.db.fb

import com.example.weatherapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class FBDatabase {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    // 🔹 Fluxo do usuário autenticado
    val user: Flow<FBUser>
        get() {
            if (auth.currentUser == null) return emptyFlow()
            return db.collection("users")
                .document(auth.currentUser!!.uid)
                .snapshots().map { it.toObject<FBUser>()!! }
        }

    // 🔹 Fluxo das cidades do usuário autenticado
    val cities: Flow<List<FBCity>>
        get() {
            if (auth.currentUser == null) return emptyFlow()
            return db.collection("users")
                .document(auth.currentUser!!.uid)
                .collection("cities")
                .snapshots().map { snapshot -> snapshot.toObjects(FBCity::class.java) }
        }

    // 🔹 Registra um novo usuário no Firestore
    suspend fun register(user: User) {
        if (auth.currentUser == null) throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid).set(user.toFBUser()).await()
    }

    // 🔹 Adiciona uma cidade ao Firestore
    suspend fun add(city: FBCity) {
        if (auth.currentUser == null) throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid).collection("cities")
            .document(city.name.toString()).set(city).await()
    }

    // 🔹 Remove uma cidade do Firestore
    suspend fun remove(city: FBCity) {
        if (auth.currentUser == null) throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid).collection("cities")
            .document(city.name.toString()).delete().await()
    }

    // 🔹 Atualiza os dados de uma cidade no Firestore
    suspend fun update(city: FBCity) {
        if (auth.currentUser == null) throw RuntimeException("Not logged in!")
        val uid = auth.currentUser!!.uid
        val changes = mapOf(
            "lat" to city.lat,
            "lng" to city.lng,
            "monitored" to city.monitored
        )
        db.collection("users").document(uid)
            .collection("cities").document(city.name!!).update(changes).await()
    }
}
