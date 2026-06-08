package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.domain.Animal
import com.lambdaTeam.sys.adoptaPerrito.entities.AnimalEntity
import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import com.lambdaTeam.sys.adoptaPerrito.repositories.AnimalRepository
import com.lambdaTeam.sys.adoptaPerrito.repositories.UsuarioRepository
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Locale
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class AnimalServiceTest {

    @Mock
    lateinit var animalRepository: AnimalRepository

    @Mock
    lateinit var usuarioRepository: UsuarioRepository

    @InjectMocks
    lateinit var animalService: AnimalService

    private val faker = Faker(Locale("es", "MX"))

    @Test
    fun agregarAnimal_GuardaYRetornaMascota_CuandoUsuarioEsValido() {

        val idUsuario = faker.number().randomDigitNotZero()
        val dueñoSimulado = UsuarioEntity(
            id_usuario = idUsuario,
            nombre = faker.name().fullName(),
            correo = faker.internet().emailAddress(),
            contrasena = faker.internet().password(),
            codigo_postal = faker.address().zipCode(),
            rol = "USER" ,
            token = null
        )
        val animalNuevo = Animal(
            nombre = faker.dog().name(),
            especie = "Perro",
            raza = faker.dog().breed(),
            descripcion = faker.lorem().sentence(),
            fotoUrl = "/imagenes/test.jpg",
            codigoPostal = faker.address().zipCode()
        )
        val animalGuardado = AnimalEntity(
            id_animal = faker.number().randomDigitNotZero(),
            nombre = animalNuevo.nombre,
            especie = animalNuevo.especie,
            raza = animalNuevo.raza,
            descripcion = animalNuevo.descripcion,
            fotoUrl = animalNuevo.fotoUrl,
            codigo_postal = animalNuevo.codigoPostal,
            estado = "DISPONIBLE",
            usuario = dueñoSimulado
        )

        `when`(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(dueñoSimulado))
        `when`(animalRepository.save(any(AnimalEntity::class.java))).thenReturn(animalGuardado)


        val resultado = animalService.agregarAnimal(animalNuevo, idUsuario)


        assertNotNull(resultado.id)
        assertEquals(animalNuevo.nombre, resultado.nombre)
        verify(usuarioRepository).findById(idUsuario)
        verify(animalRepository).save(any(AnimalEntity::class.java))
    }

    @Test
    fun eliminarAnimal_RetornaTrue_CuandoLaMascotaExiste() {

        val idAnimal = faker.number().randomDigitNotZero()
        `when`(animalRepository.existsById(idAnimal)).thenReturn(true)


        val resultado = animalService.eliminarAnimal(idAnimal)


        assertTrue(resultado)
        verify(animalRepository).deleteById(idAnimal)
    }
}