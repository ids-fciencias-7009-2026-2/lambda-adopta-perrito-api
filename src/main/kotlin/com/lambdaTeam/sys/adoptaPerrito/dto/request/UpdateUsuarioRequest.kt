package com.lambdaTeam.sys.adoptaPerrito.dto.request

/**
 * DTO utilizado para actualizar la información de un usuario registrado.
 *
 * Representa el body de una petición PUT /usuarios
 *
 * Ejemplo de JSON:
 * {
 *   "email": "nuevo-email@email.com",
 *   "codigoPostal": "11000",
 *   "password": "NuevoPassword123"
 * }
 */
data class UpdateUsuarioRequest(
    /** Nuevo correo electrónico que el usuario desea registrar
     *
     */
    val email: String,

    /** Indica el nuevo código postal asociado a la dirección del usuario.
     * Se maneja como string para mantener los 0 a la izquierda
     */
    val codigoPostal: String,

    /**
     * Nueva contraseña del usuario. Nullable, por si no desea cambiarla.
     * En un sistema real se almacenaría cifrada.
     */
    val password: String?
)

