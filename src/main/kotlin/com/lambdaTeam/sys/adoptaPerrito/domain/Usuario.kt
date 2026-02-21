package com.lambdaTeam.sys.adoptaPerrito.domain

/**
 * Modelo de dominio que representa a un Usuario dentro del sistema
 * de adopción de perros y gatos.
 *
 * Este objeto es la representación interna del negocio, no es un DTO
 * (no representa directamente un JSON del cliente) ni una entidad JPA
 * (no tiene anotaciones de base de datos).
 *
 * Campos requeridos según los lineamientos del proyecto:
 *  - nombre
 *  - email (correo electrónico válido)
 *  - codigoPostal (para ubicar al usuario en el mapa)
 *  - password es nullable porque no siempre se expone en las respuestas
 */
data class Usuario(

    /**
     * Identificador único del usuario.
     * En un sistema real lo generaría la base de datos.
     */
    val id: String,

    /**
     * Nombre completo del usuario.
     */
    var nombre: String,

    /**
     * Correo electrónico del usuario.
     * Es el principal medio de contacto entre usuarios de la plataforma.
     */
    var email: String,

    /**
     * Código postal del usuario.
     * Se usará para posicionarlo aproximadamente en el mapa.
     */
    var codigoPostal: String,

    /**
     * Contraseña del usuario.
     * Es nullable porque no se expone en las respuestas HTTP.
     * En un sistema real se almacenaría cifrada (nunca en texto plano).
     */
    var password: String? = null
)
