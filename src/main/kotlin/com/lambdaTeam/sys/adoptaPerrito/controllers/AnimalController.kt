package com.lambdaTeam.sys.adoptaPerrito.controllers

import com.lambdaTeam.sys.adoptaPerrito.services.AnimalService
import com.lambdaTeam.sys.adoptaPerrito.dto.response.ContactoResponseDTO
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/animales")
class AnimalController(private val animalService: AnimalService) {

    @GetMapping("/{id}/contacto")
    fun getContacto(@PathVariable id: Int): ResponseEntity<ContactoResponseDTO> {
        val contacto = animalService.obtenerCorreoDelDueño(id)
        return ResponseEntity.ok(contacto)
    }
}