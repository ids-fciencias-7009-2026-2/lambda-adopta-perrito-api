package com.lambdaTeam.sys.adoptaPerrito.controllers

import com.lambdaTeam.sys.adoptaPerrito.domain.Usuario
import com.lambdaTeam.sys.adoptaPerrito.domain.toUsuario
import com.lambdaTeam.sys.adoptaPerrito.dto.request.CreateUsuarioRequest
import com.lambdaTeam.sys.adoptaPerrito.dto.request.LoginRequest
import com.lambdaTeam.sys.adoptaPerrito.dto.response.LogoutResponse
import com.lambdaTeam.sys.adoptaPerrito.services.UsuarioService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.MessageDigest
import java.time.LocalDateTime

@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @Autowired
    lateinit var usuarioService: UsuarioService

    val logger: Logger = LoggerFactory.getLogger(UsuarioController::class.java)

    @GetMapping("/me")
    fun retrieveUsuario(@RequestHeader("Authorization", required = false) authHeader: String?): ResponseEntity<Any> {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(mapOf("error" to "Token no proporcionado o formato inválido"))
        }

        val token = authHeader.substring(7) // Quitamos "Bearer "
        val usuario = usuarioService.obtenerUsuarioPorToken(token)

        return if (usuario != null) {
            logger.info("Usuario recuperado desde BD: ${usuario.email}")
            // Devolvemos el usuario sin la contraseña por seguridad
            ResponseEntity.ok(usuario.copy(password = null))
        } else {
            ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))
        }
    }

    @PostMapping("/register")
    fun registrarUsuario(
        @RequestBody createUsuarioRequest: CreateUsuarioRequest
    ): ResponseEntity<Any> { // Cambiamos a <Any> para devolver el Usuario o un Mensaje de Error
        return try {
            val usuarioNuevo = createUsuarioRequest.toUsuario()
            val passwordHasheada = hashPassword(createUsuarioRequest.password)
            usuarioNuevo.password = passwordHasheada
            val usuarioGuardado = usuarioService.addNuevoUsuario(usuarioNuevo)

            logger.info("Registrando nuevo usuario en BD: ${usuarioGuardado.email}")
            ResponseEntity.ok(usuarioGuardado.copy(password = null))

        } catch (e: Exception) {
            logger.error("Error al registrar usuario: ${e.message}")

            if (e.message?.contains("ya está registrado") == true || e.message?.contains("duplicate key") == true) {
                ResponseEntity.status(400).body(mapOf("error" to "El correo electrónico ya se encuentra en uso."))
            } else {
                ResponseEntity.status(500).body(mapOf("error" to "Ocurrió un error inesperado en el servidor."))
            }
        }
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Any> {
        logger.info("Intento de login real con email: ${loginRequest.email}")

        val passwordHasheada = hashPassword(loginRequest.password)

        // El service devuelve directamente el token como String (o null si falla)
        val tokenGenerado = usuarioService.login(loginRequest.email, passwordHasheada)

        return if (tokenGenerado != null) {
            ResponseEntity.ok(mapOf(
                "mensaje" to "Login exitoso",
                "token" to tokenGenerado // Usamos directamente el string devuelto
            ))
        } else {
            logger.error("Login fallido para: ${loginRequest.email}")
            ResponseEntity.status(401).body(mapOf("error" to "Credenciales inválidas"))
        }
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader("Authorization", required = false) authHeader: String?): ResponseEntity<Any> {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(mapOf("error" to "Token no proporcionado"))
        }

        val token = authHeader.substring(7)

        // Primero buscamos al usuario para saber quién está cerrando sesión
        val usuario = usuarioService.obtenerUsuarioPorToken(token)

        return if (usuario != null) {
            // Si el usuario existe, borramos su token de la base de datos
            usuarioService.logout(token)

            val logoutResponse = LogoutResponse(
                userId = usuario.id.toString(), // ID real de la base de datos
                mensaje = "Sesión cerrada exitosamente en la BD",
                logoutDateTime = LocalDateTime.now().toString()
            )
            ResponseEntity.ok(logoutResponse)
        } else {
            ResponseEntity.status(401).body(mapOf("error" to "No se pudo cerrar sesión. Token inválido."))
        }
    }

    // Función de ayuda para encriptar contraseñas
    fun hashPassword(password: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray())
        return bytes.joinToString("") {"%02x".format(it)}
    }

    @GetMapping("/all")
    fun retrieveAllUsuarios(): ResponseEntity<Any> {
        val allUserFound = usuarioService.buscaUsuarios()
        // Nos aseguramos de no devolver contraseñas en la lista
        return ResponseEntity.ok(allUserFound.map { it.copy(password = null) })
    }

    @GetMapping("/{id}")
    fun getUsuarioById(
        @PathVariable id: Int
    ): ResponseEntity<Any> {
        val usuario = usuarioService.buscaPorId(id)
        return if (usuario != null) {
            ResponseEntity.ok(usuario.copy(password = null))
        } else {
            ResponseEntity.status(404).body("Usuario con id $id no encontrado")
        }
    }

    @GetMapping("/buscar")
    fun buscarUsuario(
        @RequestParam email: String,
        @RequestParam cp: String,
        @RequestParam nombre: String
    ): ResponseEntity<Any> {
        val todosLosUsuarios = usuarioService.buscaUsuarios()
        val resultado = todosLosUsuarios.filter { usuario ->
            usuario.email == email ||
                    usuario.codigoPostal == cp ||
                    usuario.nombre == nombre
        }
        return if (resultado.isNotEmpty()) {
            ResponseEntity.ok(resultado.map { it.copy(password = null) })
        } else {
            ResponseEntity.status(404).body("No se encontraron usuarios con esos filtros")
        }
    }
}