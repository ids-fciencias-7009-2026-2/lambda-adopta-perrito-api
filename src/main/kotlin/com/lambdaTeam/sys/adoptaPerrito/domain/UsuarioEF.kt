package com.lambdaTeam.sys.adoptaPerrito.domain

import com.lambdaTeam.sys.adoptaPerrito.dto.request.CreateUsuarioRequest
import java.util.UUID

/**
 * Extension Function que convierte un DTO CreateUsuarioRequest
 * en un objeto de dominio Usuario.
 *
 * Permite escribir:
 *     createUsuarioRequest.toUsuario()
 *
 * Esto mantiene separadas las responsabilidades:
 *  - El DTO representa datos que vienen del cliente (JSON).
 *  - El objeto de dominio representa el modelo interno del sistema.
 */
fun CreateUsuarioRequest.toUsuario(): Usuario {

    // Generamos un identificador único de forma simulada
    val id = "usr-" + UUID.randomUUID().toString()

    return Usuario(
        id = id,
        nombre = this.nombre,
        email = this.email,
        codigoPostal = this.codigoPostal
        // La contraseña no se asigna aquí; en un sistema real se cifraría antes de guardar
    )
}
