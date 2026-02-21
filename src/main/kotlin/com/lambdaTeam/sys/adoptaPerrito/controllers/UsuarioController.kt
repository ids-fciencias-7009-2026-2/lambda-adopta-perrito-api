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
    
}
