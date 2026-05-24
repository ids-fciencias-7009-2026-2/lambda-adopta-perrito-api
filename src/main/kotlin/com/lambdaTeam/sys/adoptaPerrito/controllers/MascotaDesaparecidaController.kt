package com.lambdaTeam.sys.adoptaPerrito.controllers

import com.lambdaTeam.sys.adoptaPerrito.dto.request.CrearMascotaDesaparecidaRequest
import com.lambdaTeam.sys.adoptaPerrito.services.MascotaDesaparecidaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/mascotas-desaparecidas")
class MascotaDesaparecidaController(
    private val service: MascotaDesaparecidaService
) {

    @PostMapping
    fun crear(
        @RequestBody dto: CrearMascotaDesaparecidaRequest
    ) = service.crear(dto, 1)

    @GetMapping
    fun listar() = service.listarActivas()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long) =
        service.obtenerPorId(id)

    @PutMapping("/{id}/encontrada")
    fun marcarEncontrada(@PathVariable id: Long) {
        service.marcarComoEncontrada(id)
    }

    @GetMapping("/buscar")
    fun buscarPorZona(
        @RequestParam zona: String
    ) = service.buscarPorZona(zona)
}