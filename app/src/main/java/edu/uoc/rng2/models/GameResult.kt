package edu.uoc.rng2.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

// Clase de datos que representa un resultado de juego en la base de datos.
@Entity
@Serializable
data class GameResult(
    // Clave primaria autoincremental para identificar de forma única cada resultado de juego.
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val email: String,

    // Número de turnos realizados en el juego.
    val turns: Int,

    // Fecha en la que se registró el resultado del juego (en milisegundos desde el inicio de la época).
    val date: Long,

    // Indica si el usuario ganó el juego o no.
    val userWon: Boolean,

    // La ganancia obtenida en el juego (puede ser negativa si el usuario perdió).
    val profit: Int,

    val latitude: Double? = null,

    val longitude: Double? = null,

    @Transient
    val synced: Boolean = false,
)
