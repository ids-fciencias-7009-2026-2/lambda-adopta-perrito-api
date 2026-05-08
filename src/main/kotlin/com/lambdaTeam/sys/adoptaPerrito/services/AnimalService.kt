package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.domain.Animal
import com.lambdaTeam.sys.adoptaPerrito.domain.toAnimal
import com.lambdaTeam.sys.adoptaPerrito.entities.toAnimalEntity
import com.lambdaTeam.sys.adoptaPerrito.repositories.AnimalRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AnimalService {
    val logger: Logger = LoggerFactory.getLogger(AnimalService::class.java)

    @Autowired
    lateinit var animalRepository: AnimalRepository

    fun buscarMascotas(especie: String?, raza: String?, codigoPostal: String?): List<Animal> {
        val entidades = animalRepository.findByFilters(especie, raza, codigoPostal)
        logger.info("Mascotas encontradas en la búsqueda: ${entidades.size}")
        return entidades.map { it.toAnimal() }
    }

    fun buscarPorId(id: Int): Animal? {
        val entidad = animalRepository.findById(id).orElse(null)
        logger.info("Buscando mascota con id: $id")
        return entidad?.toAnimal()
    }

    fun eliminarAnimal(id: Int): Boolean {
        return if (animalRepository.existsById(id)) {
            animalRepository.deleteById(id)
            logger.info("Mascota con id $id eliminada correctamente")
            true
        } else {
            logger.warn("No se encontró mascota con id $id para eliminar")
            false
        }
    }

    fun marcarComoAdoptado(id: Int): Animal? {
        val entidad = animalRepository.findById(id).orElse(null) ?: return null
        entidad.estado = "ADOPTADO"
        val actualizado = animalRepository.save(entidad)
        logger.info("Mascota con id $id marcada como ADOPTADO")
        return actualizado.toAnimal()
    }
}