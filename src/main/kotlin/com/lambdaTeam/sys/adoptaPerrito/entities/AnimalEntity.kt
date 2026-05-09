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

    @Column(name = "descripcion", length = 1000)
    var descripcion: String?,

    @Column(name = "foto_url", length = 1000)
    var fotoUrl: String?,

    @Column(name = "codigo_postal")
    var codigo_postal: String,


    var estado: String = "DISPONIBLE",


    @ManyToOne
    @JoinColumn(name = "id_usuario")
    var usuario: UsuarioEntity? = null
)
