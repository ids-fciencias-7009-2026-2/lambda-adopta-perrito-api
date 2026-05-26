package com.lambdaTeam.sys.adoptaPerrito.controllers

import com.lambdaTeam.sys.adoptaPerrito.dto.request.CrearMascotaDesaparecidaRequest
import com.lambdaTeam.sys.adoptaPerrito.dto.response.MascotaDesaparecidaResponseDTO
import com.lambdaTeam.sys.adoptaPerrito.services.MascotaDesaparecidaService
import com.lambdaTeam.sys.adoptaPerrito.services.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.multipart.MultipartFile

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/mascotas-desaparecidas")
class MascotaDesaparecidaController(
    private val service: MascotaDesaparecidaService,
    private val usuarioService: UsuarioService
) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun crear(
        @ModelAttribute dto: CrearMascotaDesaparecidaRequest,
        @RequestParam(value = "foto", required = false) foto: MultipartFile,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        val creada = service.crear(dto, foto, usuario.id!!.toLong())
        return ResponseEntity.ok(creada)
    }

    @GetMapping
    fun listar() = service.listarActivas()

    @GetMapping("/mis-mascotas")
    fun listarMisMascotas(
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Any> {

        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))
        val misMascotas = service.listarPorUsuario(usuario.id!!.toLong())
        return ResponseEntity.ok(misMascotas)
    }

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long) =
        service.obtenerPorId(id)

    @PutMapping("/{id}/encontrada")
    fun marcarEncontrada(@PathVariable id: Long) {
        service.marcarComoEncontrada(id)
    }

    private fun validarToken(authHeader: String?) = run {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) null
        else usuarioService.obtenerUsuarioPorToken(authHeader.substring(7))
    }

    @GetMapping("/buscar")
    fun buscarPorZona(
        @RequestParam zona: String
    ) = service.buscarPorZona(zona)
}