package com.lambdaTeam.sys.adoptaPerrito.controllers

import com.lambdaTeam.sys.adoptaPerrito.dto.request.UpdateAnimalRequest
import com.lambdaTeam.sys.adoptaPerrito.domain.Animal
import com.lambdaTeam.sys.adoptaPerrito.services.AnimalService
import com.lambdaTeam.sys.adoptaPerrito.services.UsuarioService
import com.lambdaTeam.sys.adoptaPerrito.dto.response.toAnimalResponseDTO
import com.lambdaTeam.sys.adoptaPerrito.dto.response.ContactoResponseDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.io.File
import java.util.UUID

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/animales")
class AnimalController {

    @Autowired
    lateinit var animalService: AnimalService

    @Autowired
    lateinit var usuarioService: UsuarioService

    val logger: Logger = LoggerFactory.getLogger(AnimalController::class.java)

    @GetMapping("/buscar")
    fun buscarAnimales(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestParam(required = false) especie: String?,
        @RequestParam(required = false) raza: String?,
        @RequestParam(required = false) cp: String?
    ): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        logger.info("Usuario ${usuario.email} buscando animales por filtros")
        val resultados = animalService.buscarMascotas(especie, raza, cp)

        return if (resultados.isNotEmpty()) {
            ResponseEntity.ok(resultados.map { it.toAnimalResponseDTO() })
        } else {
            ResponseEntity.status(404).body(mapOf("mensaje" to "No se encontraron mascotas con esos filtros"))
        }
    }

    @GetMapping("/{id}")
    fun obtenerAnimalPorId(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @PathVariable id: Int
    ): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        val animal = animalService.buscarPorId(id)
        return if (animal != null) {
            ResponseEntity.ok(animal.toAnimalResponseDTO())
        } else {
            ResponseEntity.status(404).body(mapOf("error" to "Mascota con id $id no encontrada"))
        }
    }

    @DeleteMapping("/{id}")
    fun eliminarAnimal(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @PathVariable id: Int
    ): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        return try {
            val eliminado = animalService.eliminarAnimal(id, usuario.id!!)
            if (eliminado) {
                ResponseEntity.ok(mapOf("mensaje" to "Mascota eliminada correctamente"))
            } else {
                ResponseEntity.status(404).body(mapOf("error" to "Mascota con id $id no encontrada"))
            }
        } catch (e: SecurityException) {
            ResponseEntity.status(403).body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}/adoptar")
    fun marcarComoAdoptado(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @PathVariable id: Int
    ): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        return try {
            val animal = animalService.marcarComoAdoptado(id, usuario.id!!)
            if (animal != null) {
                ResponseEntity.ok(animal.toAnimalResponseDTO())
            } else {
                ResponseEntity.status(404).body(mapOf("error" to "Mascota con id $id no encontrada"))
            }
        } catch (e: SecurityException) {
            ResponseEntity.status(403).body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/agregar")
    fun agregar(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam("nombre") nombre: String,
        @RequestParam("especie") especie: String,
        @RequestParam("raza", required = false) raza: String?,
        @RequestParam("descripcion", required = false) descripcion: String?,
        @RequestParam("codigoPostal") codigoPostal: String,
        @RequestParam("archivoImagen", required = false) archivoImagen: MultipartFile?
    ): ResponseEntity<Any> {

        val usuarioLogueado = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        var rutaFotoLocal = ""

        if (archivoImagen != null && !archivoImagen.isEmpty) {
            try {
                val directorio = File("uploads/mascotas/")
                if (!directorio.exists()) {
                    directorio.mkdirs()
                }

                val nombreUnico = UUID.randomUUID().toString() + "_" + archivoImagen.originalFilename
                val rutaDestino = Paths.get(directorio.absolutePath, nombreUnico)

                Files.write(rutaDestino, archivoImagen.bytes)
                rutaFotoLocal = "/imagenes/$nombreUnico"

            } catch (e: Exception) {
                logger.error("Error al guardar la imagen: ${e.message}")
                return ResponseEntity.status(500).body(mapOf("error" to "No se pudo guardar la imagen de la mascota"))
            }
        }

        val animal = Animal(
            nombre = nombre,
            especie = especie,
            raza = raza ?: "",
            descripcion = descripcion ?: "",
            codigoPostal = codigoPostal,
            fotoUrl = rutaFotoLocal
        )

        val nuevaMascota = animalService.agregarAnimal(animal, usuarioLogueado.id!!)
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMascota)
    }

    @PutMapping("/{id}/editar")
    fun editarAnimal(
        @PathVariable id: Int,
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @RequestParam(value = "nombre", required = false) nombre: String?,
        @RequestParam(value = "especie", required = false) especie: String?,
        @RequestParam(value = "raza", required = false) raza: String?,
        @RequestParam(value = "descripcion", required = false) descripcion: String?,
        @RequestParam(value = "codigoPostal", required = false) codigoPostal: String?,
        @RequestParam(value = "archivoImagen", required = false) archivoImagen: MultipartFile?
    ): ResponseEntity<Any> {
        try {
            val usuario = validarToken(authHeader)
                ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

            val animalExistente = animalService.buscarPorId(id)
                ?: return ResponseEntity.status(404).body(mapOf("error" to "Mascota con id $id no encontrada"))

            var rutaFotoFinal = animalExistente.fotoUrl

            if (archivoImagen != null && !archivoImagen.isEmpty) {
                val directorio = File("uploads/mascotas/")
                if (!directorio.exists()) {
                    directorio.mkdirs()
                }

                val nombreUnico = UUID.randomUUID().toString() + "_" + archivoImagen.originalFilename
                val rutaDestino = Paths.get(directorio.absolutePath, nombreUnico)

                Files.write(rutaDestino, archivoImagen.bytes)
                rutaFotoFinal = "/imagenes/$nombreUnico"
            }

            val request = UpdateAnimalRequest(
                nombre = nombre,
                especie = especie,
                raza = raza,
                descripcion = descripcion,
                codigoPostal = codigoPostal,
                fotoUrl = rutaFotoFinal
            )

            // Ya NO enviamos el rol
            val actualizado = animalService.editarAnimal(id, request, usuario.id!!)
            return if (actualizado != null) {
                ResponseEntity.ok(actualizado.toAnimalResponseDTO())
            } else {
                ResponseEntity.status(404).body(mapOf("error" to "Mascota con id $id no encontrada"))
            }

        } catch (e: SecurityException) {
            return ResponseEntity.status(403).body(mapOf("error" to e.message))
        } catch (e: Exception) {
            logger.error("Error crítico en editarAnimal: ${e.message}", e)
            return ResponseEntity.status(500).body(mapOf("error" to "Error interno en el servidor: ${e.message}"))
        }
    }

    @GetMapping("/{id}/contacto")
    fun getContacto(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable id: Int
    ): ResponseEntity<Any> {
        val adoptante = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "No autorizado"))

        val adoptanteEntity = usuarioService.usuarioRepository.findById(adoptante.id!!).orElse(null)
            ?: return ResponseEntity.status(404).body(mapOf("error" to "Usuario no encontrado"))

        val contacto = animalService.obtenerCorreoYNotificar(id, adoptanteEntity)
        return ResponseEntity.ok(contacto)
    }

    private fun validarToken(authHeader: String?) = run {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) null
        else usuarioService.obtenerUsuarioPorToken(authHeader.substring(7))
    }

    @GetMapping("/mis-publicaciones")
    fun misPublicaciones(@RequestHeader("Authorization") authHeader: String): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        val misAnimales = animalService.obtenerMisAnimales(usuario.id!!)
        return ResponseEntity.ok(misAnimales.map { it.toAnimalResponseDTO() })
    }
}