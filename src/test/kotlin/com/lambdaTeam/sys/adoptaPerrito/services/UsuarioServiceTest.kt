package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.domain.Usuario
import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
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

@ExtendWith(MockitoExtension::class)
class UsuarioServiceTest {

    @Mock
    lateinit var usuarioRepository: UsuarioRepository

    @InjectMocks
    lateinit var usuarioService: UsuarioService

    private val faker = Faker(Locale("es", "MX"))

    @Test
    fun addNuevoUsuario_LanzaExcepcion_CuandoElCorreoYaExiste() {

        val correoDuplicado = faker.internet().emailAddress()
        val usuarioExistente = UsuarioEntity(
            id_usuario = faker.number().randomDigitNotZero(),
            nombre = faker.name().fullName(),
            correo = correoDuplicado,
            contrasena = faker.internet().password(),
            codigo_postal = faker.address().zipCode(),
            rol = "USER",
            token = null
        )
        val nuevoUsuario = Usuario(
            nombre = faker.name().fullName(),
            email = correoDuplicado,
            password = faker.internet().password(),
            codigoPostal = faker.address().zipCode()
        )

        `when`(usuarioRepository.findAll()).thenReturn(listOf(usuarioExistente))


        val excepcion = assertThrows(Exception::class.java) {
            usuarioService.addNuevoUsuario(nuevoUsuario)
        }


        assertTrue(excepcion.message!!.contains("ya está registrado"))
        verify(usuarioRepository, never()).save(any(UsuarioEntity::class.java))
    }

    @Test
    fun login_RetornaToken_CuandoCredencialesSonCorrectas() {

        val correo = faker.internet().emailAddress()
        val passwordPlana = faker.internet().password()
        val passwordHasheada = usuarioService.hashPassword(passwordPlana)
        val idUsuario = faker.number().randomDigitNotZero()
        val usuarioEncontrado = UsuarioEntity(
            id_usuario = idUsuario,
            nombre = faker.name().fullName(),
            correo = correo,
            contrasena = passwordHasheada,
            codigo_postal = faker.address().zipCode(),
            rol = "USER",
            token = null
        )

        `when`(usuarioRepository.findUserByPasswordAndEmail(correo, passwordHasheada)).thenReturn(usuarioEncontrado)


        val tokenGenerado = usuarioService.login(correo, passwordHasheada)


        assertNotNull(tokenGenerado)
        verify(usuarioRepository).updateTokenById(eq(idUsuario), anyString())
    }
}