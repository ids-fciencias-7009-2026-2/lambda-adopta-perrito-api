package com.lambdaTeam.sys.adoptaPerrito.entities

import jakarta.persistence.*

@Entity
@Table(name = "animal")
data class AnimalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id_animal: Int? = null,

    var nombre: String,
    var especie: String,
    var raza: String?,
    var descripcion: String?,

    @Column(name = "foto_url")
    var fotoUrl: String?,

    @Column(name = "codigo_postal")
    var codigo_postal: String,
    var estado: String = "DISPONIBLE"
)
