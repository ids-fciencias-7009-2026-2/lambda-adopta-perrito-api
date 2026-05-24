package com.lambdaTeam.sys.adoptaPerrito.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "mascotas_desaparecidas")
data class MascotaDesaparecida(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val nombre: String,

    val especie: String,

    val raza: String? = null,

    val edad: Int? = null,

    val color: String? = null,

    val descripcion: String? = null,

    val zonaDesaparicion: String,

    val fechaDesaparicion: LocalDate,

    val telefonoContacto: String,

    val imagenUrl: String? = null,

    var encontrada: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    val usuario: UsuarioEntity
)