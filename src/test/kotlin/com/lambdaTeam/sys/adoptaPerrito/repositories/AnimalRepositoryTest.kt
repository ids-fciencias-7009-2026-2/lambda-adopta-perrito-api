package com.lambdaTeam.sys.adoptaPerrito.repositories

import com.lambdaTeam.sys.adoptaPerrito.entities.AnimalEntity
import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import java.util.Locale

@DataJpaTest
class AnimalRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var animalRepository: AnimalRepository

    private val faker = Faker(Locale("es", "MX"))

    @Test
    fun findByFilters_RetornaResultados_CuandoCoincideLaEspecie() {

        val usuarioDueno = UsuarioEntity(
            nombre = faker.name().fullName(),
            correo = faker.internet().emailAddress(),
            contrasena = faker.internet().password(),
            codigo_postal = faker.address().zipCode(),
            rol = "USER",
            token = null
        )
        entityManager.persist(usuarioDueno)

        val perro = AnimalEntity(
            nombre = faker.dog().name(),
            especie = "Perro",
            raza = faker.dog().breed(),
            descripcion = faker.lorem().sentence(),
            fotoUrl = "",
            codigo_postal = faker.address().zipCode(),
            estado = "DISPONIBLE",
            usuario = usuarioDueno
        )
        val gato = AnimalEntity(
            nombre = faker.cat().name(),
            especie = "Gato",
            raza = faker.cat().breed(),
            descripcion = faker.lorem().sentence(),
            fotoUrl = "",
            codigo_postal = faker.address().zipCode(),
            estado = "DISPONIBLE",
            usuario = usuarioDueno
        )
        entityManager.persist(perro)
        entityManager.persist(gato)
        entityManager.flush()


        val resultados = animalRepository.findByFilters("Perro", null, null)


        assertEquals(1, resultados.size)
        assertEquals(perro.nombre, resultados[0].nombre)
    }

    @Test
    fun findByUsuarioId_RetornaMascotas_QuePertenecenAlUsuario() {

        val usuarioDueno = UsuarioEntity(
            nombre = faker.name().fullName(),
            correo = faker.internet().emailAddress(),
            contrasena = faker.internet().password(),
            codigo_postal = faker.address().zipCode(),
            rol = "USER",
            token = null
        )
        val usuarioPersistido = entityManager.persist(usuarioDueno)

        val mascota = AnimalEntity(
            nombre = faker.dog().name(),
            especie = "Perro",
            raza = faker.dog().breed(),
            descripcion = faker.lorem().sentence(),
            fotoUrl = "",
            codigo_postal = faker.address().zipCode(),
            estado = "DISPONIBLE",
            usuario = usuarioPersistido
        )
        entityManager.persist(mascota)
        entityManager.flush()


        val resultados = animalRepository.findByUsuarioId(usuarioPersistido.id_usuario!!)


        assertEquals(1, resultados.size)
        assertEquals(mascota.nombre, resultados[0].nombre)
    }
}