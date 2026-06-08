package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.entities.MascotaDesaparecida
import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import com.lambdaTeam.sys.adoptaPerrito.repositories.MascotaDesaparecidaRepository
import com.lambdaTeam.sys.adoptaPerrito.repositories.UsuarioRepository
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.util.Locale
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class MascotaDesaparecidaServiceTest {

    @Mock
    lateinit var mascotaRepository: MascotaDesaparecidaRepository

    @Mock
    lateinit var usuarioRepository: UsuarioRepository

    @InjectMocks
    lateinit var mascotaDesaparecidaService: MascotaDesaparecidaService

    private val faker = Faker(Locale("es", "MX"))

    @Test
    fun marcarComoEncontrada_ActualizaEstadoTrue_CuandoMascotaExiste() {

        val idMascota = faker.number().randomNumber()

        val usuarioSimulado = UsuarioEntity(
            id_usuario = faker.number().randomDigitNotZero(),
            nombre = faker.name().fullName(),
            correo = faker.internet().emailAddress(),
            contrasena = faker.internet().password(),
            codigo_postal = faker.address().zipCode(),
            rol = "USER",
            token = null
        )

        val mascotaExtraviada = MascotaDesaparecida(
            id = idMascota,
            nombre = faker.dog().name(),
            especie = "Perro",
            raza = faker.dog().breed(),
            edad = faker.number().numberBetween(1, 15),
            color = faker.color().name(),
            descripcion = faker.lorem().sentence(),
            zonaDesaparicion = faker.address().streetName(),
            fechaDesaparicion = LocalDate.parse("2023-10-01"),
            telefonoContacto = faker.phoneNumber().cellPhone(),
            imagenUrl = null,
            encontrada = false,
            usuario = usuarioSimulado
        )

        `when`(mascotaRepository.findById(idMascota)).thenReturn(Optional.of(mascotaExtraviada))


        mascotaDesaparecidaService.marcarComoEncontrada(idMascota)


        assertTrue(mascotaExtraviada.encontrada)
        verify(mascotaRepository).save(mascotaExtraviada)
    }

    @Test
    fun obtenerPorId_LanzaExcepcion_CuandoNoExiste() {

        val idInvalido = faker.number().randomNumber()
        `when`(mascotaRepository.findById(idInvalido)).thenReturn(Optional.empty())


        val excepcion = assertThrows(RuntimeException::class.java) {
            mascotaDesaparecidaService.obtenerPorId(idInvalido)
        }


        assertEquals("Mascota no encontrada", excepcion.message)
    }
}