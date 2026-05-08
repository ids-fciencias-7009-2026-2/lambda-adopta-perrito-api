package com.lambdaTeam.sys.adoptaPerrito.dto.response

import com.lambdaTeam.sys.adoptaPerrito.domain.Animal

fun Animal.toAnimalResponseDTO(): AnimalResponseDTO {
    return AnimalResponseDTO(
        id = this.id ?: 0,
        nombre = this.nombre,
        especie = this.especie,
        raza = this.raza,
        descripcion = this.descripcion,
        fotoUrl = this.fotoUrl,
        codigoPostal = this.codigoPostal,
        estado = this.estado
    )
}