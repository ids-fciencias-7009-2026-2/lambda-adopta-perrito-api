package com.lambdaTeam.sys.adoptaPerrito.controllers

import com.lambdaTeam.sys.adoptaPerrito.domain.Usuario
import com.lambdaTeam.sys.adoptaPerrito.domain.toUsuario
import com.lambdaTeam.sys.adoptaPerrito.dto.request.CreateUsuarioRequest
import com.lambdaTeam.sys.adoptaPerrito.dto.request.LoginRequest
import com.lambdaTeam.sys.adoptaPerrito.dto.request.UpdateUsuarioRequest
import com.lambdaTeam.sys.adoptaPerrito.dto.response.LogoutResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * Controlador encargado de exponer los endpoints REST relacionados
 * con la gestión de usuarios de la plataforma de adopción de perros y gatos.
 *
 * En esta versión inicial (Práctica 1) se utilizan datos simulados ("fake")
 * sin conexión real a base de datos ni seguridad real.
 *
 * Endpoints implementados:
 *  - GET  /usuarios/me        → Obtiene el usuario autenticado
 *  - POST /usuarios/register  → Registra un nuevo usuario
 *  - POST /usuarios/login     → Auténtica a un usuario
 *  - POST /usuarios/logout    → Cierra la sesión del usuario
 *  - PUT  /usuarios           → Actualiza la información del usuario
 */
@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    val logger: Logger = LoggerFactory.getLogger(UsuarioController::class.java)

    /**
     * Endpoint que devuelve la información del usuario autenticado.
     *
     * URL:    GET http://localhost:8080/usuarios/me
     * Método: GET
     *
     * @return ResponseEntity con un objeto Usuario y código HTTP 200 (OK).
     */
    @GetMapping("/me")
    fun retrieveUsuario(): ResponseEntity<Usuario> {

        // Usuario simulado — en un sistema real se obtendría de la sesión/token
        val usuarioFake = Usuario(
            id = "usr-001",
            nombre = "Ana García",
            email = "ana.garcia@email.com",
            codigoPostal = "06600"
        )

        logger.info("Usuario recuperado: $usuarioFake")

        return ResponseEntity.ok(usuarioFake)
    }

    /**
     * Endpoint para registrar un nuevo usuario en la plataforma.
     *
     * URL:    POST http://localhost:8080/usuarios/register
     * Método: POST
     *
     * Body esperado (JSON):
     * {
     *   "nombre": "Ana García",
     *   "email": "ana@email.com",
     *   "codigoPostal": "06600",
     *   "password": "MiPassword123"
     * }
     *
     * @param createUsuarioRequest DTO con los datos del nuevo usuario.
     * @return ResponseEntity con el usuario creado y código HTTP 200 (OK).
     */
    @PostMapping("/register")
    fun registrarUsuario(
        @RequestBody createUsuarioRequest: CreateUsuarioRequest
    ): ResponseEntity<Usuario> {

        // Conversión de DTO a objeto de dominio usando extension function
        val usuarioNuevo = createUsuarioRequest.toUsuario()

        logger.info("Registrando nuevo usuario: $usuarioNuevo")

        // En esta etapa no se guarda en BD, solo se simula el registro
        return ResponseEntity.ok(usuarioNuevo)
    }

    /**
     * Endpoint que simula la autenticación de un usuario.
     *
     * Compara las credenciales recibidas contra un usuario ficticio.
     *
     * URL:    POST http://localhost:8080/usuarios/login
     * Método: POST
     *
     * Body esperado (JSON):
     * {
     *   "email": "ana@email.com",
     *   "password": "MiPassword123"
     * }
     *
     * @param loginRequest DTO con las credenciales del usuario.
     * @return HTTP 200 si las credenciales son correctas,
     *         HTTP 401 si son incorrectas.
     */
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Any> {

        // Usuario simulado que representa el registro en el sistema
        val usuarioFake = Usuario(
            id = "usr-001",
            nombre = "Ana García",
            email = "ana@email.com",
            codigoPostal = "06600",
            password = "MiPassword123"
        )

        logger.info("Intento de login con email: ${loginRequest.email}")

        return if (usuarioFake.email == loginRequest.email && usuarioFake.password == loginRequest.password) {
            logger.info("Login exitoso para: ${loginRequest.email}")
            // HTTP 200 → autenticación exitosa
            ResponseEntity.ok(usuarioFake.copy(password = null)) // No retornamos el password
        } else {
            logger.error("Login fallido para: ${loginRequest.email}")
            // HTTP 401 → Unauthorized (credenciales inválidas)
            ResponseEntity.status(401).build()
        }
    }

    /**
     * Endpoint que simula el cierre de sesión del usuario.
     *
     * URL:    POST http://localhost:8080/usuarios/logout
     * Método: POST
     *
     * @return ResponseEntity con información del logout y código HTTP 200 (OK).
     */
    @PostMapping("/logout")
    fun logout(): ResponseEntity<LogoutResponse> {

        // En un sistema real, aquí se invalidaría el token de sesión
        val usuarioFake = Usuario(
            id = "usr-001",
            nombre = "Ana García",
            email = "ana@email.com",
            codigoPostal = "06600"
        )

        val logoutResponse = LogoutResponse(
            userId = usuarioFake.id,
            mensaje = "Sesión cerrada exitosamente",
            logoutDateTime = LocalDateTime.now().toString()
        )

        logger.info("Logout realizado para usuario: ${usuarioFake.id} a las ${logoutResponse.logoutDateTime}")

        return ResponseEntity.ok(logoutResponse)
    }

    /**
     * Endpoint que actualiza la información del usuario autenticado.
     *
     * Permite modificar email, código postal y contraseña.
     *
     * URL:    PUT http://localhost:8080/usuarios
     * Método: PUT
     *
     * Body esperado (JSON):
     * {
     *   "email": "nuevo-email@email.com",
     *   "codigoPostal": "11000",
     *   "password": "NuevoPassword123"
     * }
     *
     * @param updateUsuarioRequest DTO con los nuevos datos.
     * @return ResponseEntity con el usuario actualizado y código HTTP 200 (OK).
     */
    @PutMapping
    fun actualizarUsuario(
        @RequestBody updateUsuarioRequest: UpdateUsuarioRequest
    ): ResponseEntity<Usuario> {

        // Simulación del usuario encontrado en el sistema
        val usuarioFake = Usuario(
            id = "usr-001",
            nombre = "Ana García",
            email = "ana@email.com",
            codigoPostal = "06600"
        )

        logger.info("Usuario encontrado para actualizar: $usuarioFake")
        logger.info("Datos de actualización recibidos: $updateUsuarioRequest")

        // Simulación de la actualización de datos
        usuarioFake.email = updateUsuarioRequest.email
        usuarioFake.codigoPostal = updateUsuarioRequest.codigoPostal
        if (updateUsuarioRequest.password != null) {
            usuarioFake.password = updateUsuarioRequest.password
        }

        logger.info("Usuario actualizado: $usuarioFake")

        // Retornamos el usuario sin el password
        return ResponseEntity.ok(usuarioFake.copy(password = null))
    }
}