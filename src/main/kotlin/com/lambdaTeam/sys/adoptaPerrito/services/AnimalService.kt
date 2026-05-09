package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.repositories.AnimalRepository
import com.lambdaTeam.sys.adoptaPerrito.dto.response.ContactoResponseDTO
import org.springframework.stereotype.Service

@Service
class AnimalService(private val animalRepository: AnimalRepository) {

    fun obtenerCorreoDelDueño(id: Int): ContactoResponseDTO {
        val animal = animalRepository.findById(id)
            .orElseThrow { NoSuchElementException("No se encontró el animal con id: $id") }
        
        // Obtenemos el correo del objeto usuario asociado
        val email = animal.usuario?.correo ?: "Correo no disponible"
        return ContactoResponseDTO(email)
    }
}