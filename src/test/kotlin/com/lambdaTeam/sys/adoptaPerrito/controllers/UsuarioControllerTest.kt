package com.lambdaTeam.sys.adoptaPerrito.controllers

import tools.jackson.databind.ObjectMapper
import com.lambdaTeam.sys.adoptaPerrito.dto.request.LoginRequest
import com.lambdaTeam.sys.adoptaPerrito.services.UsuarioService
import net.datafaker.Faker
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Locale

@WebMvcTest(UsuarioController::class)
class UsuarioControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var usuarioService: UsuarioService

    private val faker = Faker(Locale("es", "MX"))

    @Test
    fun login_RetornaTokenYOk_CuandoCredencialesSonCorrectas() {

        val correo = faker.internet().emailAddress()
        val passwordPlana = faker.internet().password()
        val loginRequest = LoginRequest(correo, passwordPlana)
        val tokenGenerado = faker.internet().uuid()

        `when`(usuarioService.login(anyString(), anyString())).thenReturn(tokenGenerado)


        mockMvc.perform(
            post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )

            .andExpect(status().isOk)
            .andExpect(jsonPath("$.mensaje").value("Login exitoso"))
            .andExpect(jsonPath("$.token").value(tokenGenerado))
    }

    @Test
    fun login_RetornaNoAutorizado_CuandoCredencialesSonIncorrectas() {

        val loginRequest = LoginRequest(faker.internet().emailAddress(), "claveEquivocada")

        `when`(usuarioService.login(anyString(), anyString())).thenReturn(null)


        mockMvc.perform(
            post("/usuarios/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )

            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.error").value("Credenciales inválidas"))
    }
}