package com.lambdaTeam.sys.adoptaPerrito.domain

/**
 * Modelo de dominio para las mascotas en adopción.
 */
data class Animal(
    val id: Int? = null,
    var nombre: String,
    var especie: String,
    var raza: String? = null,
    var descripcion: String? = null,
    var fotoUrl: String? = null,
    var codigoPostal: String,
    var estado: String = "DISPONIBLE",
    var usuario: Usuario? = null
)