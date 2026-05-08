package com.lambdaTeam.sys.adoptaPerrito.domain

import com.lambdaTeam.sys.adoptaPerrito.entities.AnimalEntity

fun AnimalEntity.toAnimal(): Animal {
    return Animal(
        id = this.id_animal,
        nombre = this.nombre,
        especie = this.especie,
        raza = this.raza,
        descripcion = this.descripcion,
        fotoUrl = this.fotoUrl,
        codigoPostal = this.codigo_postal,
        estado = this.estado
    )
}