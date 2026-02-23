package com.lambdaTeam.sys.adoptaPerrito.dto.response

/**
 * DTO de respuesta para el endpoint de logout.
 *
 * Representa el body de la respuesta HTTP de POST /usuarios/logout
 *
 * Ejemplo de JSON de respuesta:
 * {
 *   "userId": "usr-abc123",
 *   "mensaje": "Sesión cerrada exitosamente",
 *   "logoutDateTime": "2026-02-19T14:30:00"
 * }
 */
data class LogoutResponse(


    /* Representa el ID único del usuario dentro del sistema
     * Permite confirmar qué usuario realizó la operación de logout
     */
    val userId: String,


    /* Mensaje informativo del resultado del logout.
     * Ejemplo: "Sesión cerrada exitosamente"
     */
    val mensaje: String,


    /* Fecha y hora en la que se realizó el cierre de sesión
     * Indica el momento exacto en el que el sistema procesó el logout.
     */
    val logoutDateTime: String
)