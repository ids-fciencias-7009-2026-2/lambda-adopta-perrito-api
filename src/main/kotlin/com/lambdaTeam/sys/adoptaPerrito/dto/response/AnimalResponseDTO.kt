package com.lambdaTeam.sys.adoptaPerrito.dto.response

data class AnimalResponseDTO(
    val id: Int,
    val nombre: String,
    val especie: String,
    val raza: String?,
    val descripcion: String?,
    val fotoUrl: String?,
    val codigoPostal: String,
    val estado: String
)