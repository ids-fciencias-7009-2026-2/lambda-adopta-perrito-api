package com.lambdaTeam.sys.adoptaPerrito.controllers

import com.lambdaTeam.sys.adoptaPerrito.dto.request.CrearMascotaDesaparecidaRequest
import com.lambdaTeam.sys.adoptaPerrito.services.MascotaDesaparecidaService
import com.lambdaTeam.sys.adoptaPerrito.services.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/mascotas-desaparecidas")
class MascotaDesaparecidaController(
    private val service: MascotaDesaparecidaService,
    private val usuarioService: UsuarioService
) {

    @PostMapping
    fun crear(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody dto: CrearMascotaDesaparecidaRequest
    ): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "No autorizado"))

        return ResponseEntity.ok(service.crear(dto, usuario.id_usuario!!.toLong()))
    }

    @GetMapping
    fun listar() = service.listarActivas()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long) = service.obtenerPorId(id)

    @PutMapping("/{id}/encontrada")
    fun marcarEncontrada(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable id: Long
    ): ResponseEntity<Any> {
        // Aquí validamos que el usuario esté logueado antes de marcarla como encontrada
        validarToken(authHeader) ?: return ResponseEntity.status(401).body(mapOf("error" to "No autorizado"))
        service.marcarComoEncontrada(id)
        return ResponseEntity.ok(mapOf("mensaje" to "Estado actualizado"))
    }

    @GetMapping("/buscar")
    fun buscarPorZona(@RequestParam zona: String) = service.buscarPorZona(zona)

    // Trae exclusivamente las mascotas perdidas del usuario logueado
    @GetMapping("/mis-publicaciones")
    fun misPublicaciones(@RequestHeader("Authorization") authHeader: String): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        val misPerdidas = service.obtenerMisMascotasPerdidas(usuario.id_usuario!!)
        return ResponseEntity.ok(misPerdidas)
    }

    // Función auxiliar para leer el token y buscar al usuario en la BD
    private fun validarToken(authHeader: String?) = run {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) null
        else usuarioService.usuarioRepository.findByToken(authHeader.substring(7))
    }
}