package com.lambdaTeam.sys.adoptaPerrito.controllers

import com.lambdaTeam.sys.adoptaPerrito.domain.Usuario
import com.lambdaTeam.sys.adoptaPerrito.dto.response.MascotaDesaparecidaResponseDTO
import com.lambdaTeam.sys.adoptaPerrito.services.MascotaDesaparecidaService
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
import java.time.LocalDate
import java.util.Locale

@WebMvcTest(MascotaDesaparecidaController::class)
class MascotaDesaparecidaControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var service: MascotaDesaparecidaService

    @MockitoBean
    lateinit var usuarioService: UsuarioService

    private val faker = Faker(Locale("es", "MX"))

    @Test
    fun listarMisMascotas_RetornaMascotas_CuandoTokenEsValido() {

        val token = faker.internet().uuid()
        val usuarioSimulado = Usuario(
            id = faker.number().randomDigitNotZero(),
            nombre = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            codigoPostal = faker.address().zipCode(),
            rol = "USER"
        )
        val mascotaRespuesta = MascotaDesaparecidaResponseDTO(
            id = faker.number().randomNumber(),
            nombre = faker.dog().name(),
            especie = "Perro",
            raza = faker.dog().breed(),
            edad = faker.number().numberBetween(1, 10),
            color = faker.color().name(),
            descripcion = faker.lorem().sentence(),
            zonaDesaparicion = faker.address().streetName(),
            fechaDesaparicion = LocalDate.parse("2023-10-01"),
            telefonoContacto = faker.phoneNumber().cellPhone(),
            imagenUrl = null,
            encontrada = false
        )

        `when`(usuarioService.obtenerUsuarioPorToken(token)).thenReturn(usuarioSimulado)
        `when`(service.listarPorUsuario(usuarioSimulado.id!!.toLong())).thenReturn(listOf(mascotaRespuesta))


        mockMvc.perform(
            get("/mascotas-desaparecidas/mis-mascotas")
                .header("Authorization", "Bearer $token")
        )

            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].nombre").value(mascotaRespuesta.nombre))
            .andExpect(jsonPath("$[0].zonaDesaparicion").value(mascotaRespuesta.zonaDesaparicion))
    }
}