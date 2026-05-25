package com.lambdaTeam.sys.adoptaPerrito.dto.request

import java.time.LocalDate

data class CrearMascotaDesaparecidaRequest(
    val nombre: String,

    val especie: String,

    val raza: String? = null,

    val edad: Int? = null,

    val color: String? = null,

    val descripcion: String? = null,

    val zonaDesaparicion: String,

    val fechaDesaparicion: LocalDate,

    val telefonoContacto: String,

    val imagenUrl: String? = null
)