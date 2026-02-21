package com.lambdaTeam.sys.adoptaPerrito.dto.request

/**
 * DTO utilizado para recibir las credenciales del usuario
 * cuando intenta autenticarse en la plataforma.
 *
 * Representa el body de una petición POST /usuarios/login
 *
 * Ejemplo de JSON:
 * {
 *   "email": "ana@email.com",
 *   "password": "MiPassword123"
 * }
 */
data class LoginRequest(

    /** Correo electrónico del usuario que intenta autenticarse. */
    val email: String,

    /**
     * Contraseña ingresada por el usuario.
     * En un sistema real se compararía contra el hash almacenado,
     * nunca en texto plano.
     */
    val password: String
)
