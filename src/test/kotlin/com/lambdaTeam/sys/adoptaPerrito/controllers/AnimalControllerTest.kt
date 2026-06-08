package com.lambdaTeam.sys.adoptaPerrito.controllers

import com.lambdaTeam.sys.adoptaPerrito.domain.Animal
import com.lambdaTeam.sys.adoptaPerrito.domain.Usuario
import com.lambdaTeam.sys.adoptaPerrito.services.AnimalService
import com.lambdaTeam.sys.adoptaPerrito.services.EmailService
import com.lambdaTeam.sys.adoptaPerrito.services.UsuarioService
import net.datafaker.Faker
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Locale

@WebMvcTest(AnimalController::class)
class AnimalControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var animalService: AnimalService

    @MockitoBean
    lateinit var usuarioService: UsuarioService

    @MockitoBean
    lateinit var emailService: EmailService

    private val faker = Faker(Locale("es", "MX"))

    @Test
    fun buscarAnimales_RetornaLista_CuandoAutenticadoYExistenResultados() {

        val token = faker.internet().uuid()
        val usuarioSimulado = Usuario(
            id = faker.number().randomDigitNotZero(),
            nombre = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            codigoPostal = faker.address().zipCode(),
            rol = "USER"
        )
        val animalEncontrado = Animal(
            id = faker.number().randomDigitNotZero(),
            nombre = faker.dog().name(),
            especie = "Perro",
            raza = faker.dog().breed(),
            descripcion = faker.lorem().sentence(),
            fotoUrl = "/ruta/foto.jpg",
            codigoPostal = faker.address().zipCode(),
            estado = "DISPONIBLE"
        )

        `when`(usuarioService.obtenerUsuarioPorToken(token)).thenReturn(usuarioSimulado)
        `when`(animalService.buscarMascotas("Perro", null, null)).thenReturn(listOf(animalEncontrado))


        mockMvc.perform(
            get("/animales/buscar")
                .header("Authorization", "Bearer $token")
                .param("especie", "Perro")
        )

            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].nombre").value(animalEncontrado.nombre))
            .andExpect(jsonPath("$[0].especie").value("Perro"))
    }

    @Test
    fun buscarAnimales_RetornaNoAutorizado_CuandoFaltaToken() {

        mockMvc.perform(
            get("/animales/buscar")
                .param("especie", "Gato")
        )

            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.error").exists())
    }
}