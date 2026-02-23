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
     * Representa la nueva contraseñ adel usuario.
     * Puede ser null si el usuario solo quiere modificar el correo o el código postal.
     */
    val password: String?
)

