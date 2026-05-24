package com.lambdaTeam.sys.adoptaPerrito.dto.response

import java.time.LocalDate

data class MascotaDesaparecidaResponseDTO(
    val id: Long,
    val nombre: String,
    val especie: String,
    val raza: String?,
    val edad: Int?,
    val color: String?,
    val descripcion: String?,
    val zonaDesaparicion: String,
    val fechaDesaparicion: LocalDate,
    val telefonoContacto: String,
    val imagenUrl: String?,
    val encontrada: Boolean
)