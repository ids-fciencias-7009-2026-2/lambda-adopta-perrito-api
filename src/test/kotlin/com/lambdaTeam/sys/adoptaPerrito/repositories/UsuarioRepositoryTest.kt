package com.lambdaTeam.sys.adoptaPerrito.repositories

import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import java.util.Locale

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var usuarioRepository: UsuarioRepository

    private val faker = Faker(Locale("es", "MX"))

    @Test
    fun findUserByPasswordAndEmail_RetornaUsuario_CuandoCredencialesSonCorrectas() {

        val correoPrueba = faker.internet().emailAddress()
        val contrasenaHasheada = faker.internet().password()

        val usuario = UsuarioEntity(
            nombre = faker.name().fullName(),
            correo = correoPrueba,
            contrasena = contrasenaHasheada,
            codigo_postal = faker.address().zipCode(),
            rol = "USER",
            token = null
        )
        entityManager.persist(usuario)
        entityManager.flush()


        val usuarioEncontrado = usuarioRepository.findUserByPasswordAndEmail(correoPrueba, contrasenaHasheada)


        assertNotNull(usuarioEncontrado)
        assertEquals(correoPrueba, usuarioEncontrado?.correo)
    }

    @Test
    fun updateTokenById_ActualizaElToken_ParaElUsuarioIndicado() {

        val usuario = UsuarioEntity(
            nombre = faker.name().fullName(),
            correo = faker.internet().emailAddress(),
            contrasena = faker.internet().password(),
            codigo_postal = faker.address().zipCode(),
            rol = "USER",
            token = null
        )
        val usuarioPersistido = entityManager.persist(usuario)
        entityManager.flush()

        val nuevoToken = faker.internet().uuid()


        usuarioRepository.updateTokenById(usuarioPersistido.id_usuario!!, nuevoToken)
        entityManager.clear()


        val usuarioActualizado = entityManager.find(UsuarioEntity::class.java, usuarioPersistido.id_usuario!!)
        assertEquals(nuevoToken, usuarioActualizado?.token)
    }
}