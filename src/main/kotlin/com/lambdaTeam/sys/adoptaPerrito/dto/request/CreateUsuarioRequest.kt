package com.lambdaTeam.sys.adoptaPerrito.dto.request

/**
 * DTO utilizado para recibir los datos necesarios para registrar
 * un nuevo usuario en la plataforma de adopción.
 *
 * Representa el body de una petición POST /usuarios/register
 *
 * Ejemplo de JSON:
 * {
 *   "nombre": "Ana García",
 *   "email": "ana@email.com",
 *   "codigoPostal": "06600",
 *   "password": "MiPassword123"
 * }
 */
data class CreateUsuarioRequest(

    /** Nombre completo del usuario. */
    val nombre: String,

    /** Correo electrónico válido del usuario. */
    val email: String,

    /** Código postal del usuario, para ubicarlo en el mapa de la plataforma. */
    val codigoPostal: String,

    /**
     * Contraseña del usuario.
     * En un sistema real se almacenaría cifrada, nunca en texto plano.
     */
    val password: String
)
