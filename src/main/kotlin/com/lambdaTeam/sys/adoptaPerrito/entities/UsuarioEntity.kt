package com.lambdaTeam.sys.adoptaPerrito.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.persistence.Id

@Entity
@Table(name = "usuario")

data class UsuarioEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id_usuario: Int? = null,
    var nombre: String,
    var correo: String,
    var codigo_postal: String,
    var contrasena: String,
    var token: String?
)