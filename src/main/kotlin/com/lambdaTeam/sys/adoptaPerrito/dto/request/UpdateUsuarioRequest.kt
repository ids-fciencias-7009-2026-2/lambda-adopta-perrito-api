package com.lambdaTeam.sys.adoptaPerrito.dto.request

/**
 * DTO utilizado para actualizar la información de un usuario autenticado.
 *
 * Representa el body de una petición PUT /usuarios
 *
 * A diferencia del registro, todos los campos son opcionales (nullable),
 * ya que el usuario puede decidir actualizar solo ciertos datos.
 *
 * Ejemplo de JSON:
 * {
 *   "nombre": "Juan Pérez",
 *   "email": "nuevo-email@email.com",
 *   "codigoPostal": "11000",
 *   "password": "NuevoPassword123"
 * }
 */
data class UpdateUsuarioRequest(

    /**
     * Nuevo nombre del usuario.
     * Nullable: si no se envía, no se modifica.
     */
    val nombre: String?,

    /**
     * Nuevo correo electrónico que el usuario desea registrar.
     * Nullable: si no se envía, se mantiene el actual.
     */
    val email: String?,

    /**
     * Nuevo código postal asociado a la dirección del usuario.
     * Se maneja como String para preservar ceros a la izquierda.
     * Nullable: si no se envía, no se modifica.
     */
    val codigoPostal: String?,

    /**
     * Nueva contraseña del usuario.
     * Nullable: permite no cambiar la contraseña si no se desea.
     *
     * Nota: La contraseña debe ser encriptada antes de almacenarse.
     */
    val password: String?
)
