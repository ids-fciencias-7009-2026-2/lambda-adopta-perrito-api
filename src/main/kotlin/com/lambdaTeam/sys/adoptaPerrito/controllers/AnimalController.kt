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

        if (usuario.rol != "ADMIN") {
            return ResponseEntity.status(403).body(mapOf("error" to "No tienes permisos para eliminar mascotas"))
        }

        val eliminado = animalService.eliminarAnimal(id)
        return if (eliminado) {
            ResponseEntity.ok(mapOf("mensaje" to "Mascota eliminada correctamente"))
        } else {
            ResponseEntity.status(404).body(mapOf("error" to "Mascota con id $id no encontrada"))
        }
    }


    @PutMapping("/{id}/adoptar")
    fun marcarComoAdoptado(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @PathVariable id: Int
    ): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        if (usuario.rol != "ADMIN") {
            return ResponseEntity.status(403).body(mapOf("error" to "No tienes permisos para marcar adopciones"))
        }

        val animal = animalService.marcarComoAdoptado(id)
        return if (animal != null) {
            ResponseEntity.ok(animal.toAnimalResponseDTO())
        } else {
            ResponseEntity.status(404).body(mapOf("error" to "Mascota con id $id no encontrada"))
        }
    }


    @PostMapping("/agregar")
    fun agregar(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody animal: Animal
    ): ResponseEntity<Any> {
        val usuarioLogueado = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        // Se pasa el ID del usuario logueado al service para la llave foránea
        val nuevaMascota = animalService.agregarAnimal(animal, usuarioLogueado.id!!)
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMascota)
    }


    @PutMapping("/{id}/editar")
    fun editarAnimal(
        @RequestHeader("Authorization", required = false) authHeader: String?,
        @PathVariable id: Int,
        @RequestBody request: UpdateAnimalRequest
    ): ResponseEntity<Any> {
        val usuario = validarToken(authHeader)
            ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido o sesión expirada"))

        return try {
            val actualizado = animalService.editarAnimal(id, request, usuario.id!!, usuario.rol)
            if (actualizado != null) {
                ResponseEntity.ok(actualizado.toAnimalResponseDTO())
            } else {
                ResponseEntity.status(404).body(mapOf("error" to "Mascota con id $id no encontrada"))
            }
        } catch (e: SecurityException) {
            ResponseEntity.status(403).body(mapOf("error" to e.message))
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

    // Función de ayuda para validar el token y obtener el usuario
    private fun validarToken(authHeader: String?) = run {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) null
        else usuarioService.obtenerUsuarioPorToken(authHeader.substring(7))
    }
}