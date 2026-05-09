package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.domain.Animal
import com.lambdaTeam.sys.adoptaPerrito.domain.toAnimal
import com.lambdaTeam.sys.adoptaPerrito.dto.response.ContactoResponseDTO
import com.lambdaTeam.sys.adoptaPerrito.entities.AnimalEntity
import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import com.lambdaTeam.sys.adoptaPerrito.repositories.AnimalRepository
import com.lambdaTeam.sys.adoptaPerrito.repositories.UsuarioRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AnimalService {

    val logger: Logger = LoggerFactory.getLogger(AnimalService::class.java)

    @Autowired
    lateinit var animalRepository: AnimalRepository

    @Autowired
    lateinit var usuarioRepository: UsuarioRepository

    fun buscarMascotas(
        especie: String?,
        raza: String?,
        codigoPostal: String?
    ): List<Animal> {

        val entidades = animalRepository.findByFilters(
            especie,
            raza,
            codigoPostal
        )

        logger.info("Mascotas encontradas: ${entidades.size}")

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

            logger.info("Mascota eliminada con id: $id")

            true

        } else {

            logger.warn("No se encontró mascota con id: $id")

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

    fun agregarAnimal(animal: Animal, idUsuario: Int): Animal {

        logger.info("Agregando mascota para usuario: $idUsuario")

        val dueño = usuarioRepository.findById(idUsuario)
            .orElseThrow {
                NoSuchElementException(
                    "No se encontró usuario con id: $idUsuario"
                )
            }

        val entidad = AnimalEntity(
            nombre = animal.nombre,
            especie = animal.especie,
            raza = animal.raza,
            descripcion = animal.descripcion,
            fotoUrl = animal.fotoUrl,
            codigo_postal = animal.codigoPostal,
            estado = animal.estado,
            usuario = dueño
        )

        logger.info("Usuario asignado: ${dueño.correo}")

        val guardado = animalRepository.save(entidad)

        logger.info(
            "Mascota guardada correctamente con id: ${guardado.id_animal}"
        )

        return guardado.toAnimal()
    }



    fun obtenerCorreoYNotificar(
        id: Int,
        adoptante: UsuarioEntity
    ): ContactoResponseDTO {

        val animal = animalRepository.findById(id)
            .orElseThrow {
                NoSuchElementException(
                    "No se encontró el animal con id: $id"
                )
            }

        val emailDestino =
            animal.usuario?.correo ?: "Correo no disponible"

        logger.info(
            "Notificación enviada a: $emailDestino"
        )

        println("\n" + "=".repeat(40))
        println("📧 [ALERTA DE INTERÉS]")
        println("DE: ${adoptante.correo}")
        println("PARA EL DUEÑO: $emailDestino")
        println(
            "MENSAJE: ¡Hola! El usuario ${adoptante.nombre} " +
                    "está interesado en ${animal.nombre}."
        )
        println("=".repeat(40) + "\n")

        return ContactoResponseDTO(emailDestino)
    }
}