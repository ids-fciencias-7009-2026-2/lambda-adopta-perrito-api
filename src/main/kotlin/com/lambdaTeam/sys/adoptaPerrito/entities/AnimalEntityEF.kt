package com.lambdaTeam.sys.adoptaPerrito.entities

import com.lambdaTeam.sys.adoptaPerrito.domain.Animal

fun Animal.toAnimalEntity(): AnimalEntity {
    return AnimalEntity(
        id_animal = this.id,
        nombre = this.nombre,
        especie = this.especie,
        raza = this.raza,
        descripcion = this.descripcion,
        fotoUrl = this.fotoUrl,
        codigo_postal = this.codigoPostal,
        estado = this.estado,
        usuario = this.usuario?.toUsuarioEntity()

    )
}