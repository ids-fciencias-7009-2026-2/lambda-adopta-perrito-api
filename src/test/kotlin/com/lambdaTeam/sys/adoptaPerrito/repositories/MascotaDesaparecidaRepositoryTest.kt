package com.lambdaTeam.sys.adoptaPerrito.repositories

import com.lambdaTeam.sys.adoptaPerrito.entities.MascotaDesaparecida
import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import java.time.LocalDate
import java.util.Locale

@DataJpaTest
class MascotaDesaparecidaRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var mascotaRepository: MascotaDesaparecidaRepository

    private val faker = Faker(Locale("es", "MX"))

    @Test
    fun findByEncontradaFalse_RetornaSoloMascotas_QueAunEstanPerdidas() {

        val usuario = UsuarioEntity(
            nombre = faker.name().fullName(),
            correo = faker.internet().emailAddress(),
            contrasena = faker.internet().password(),
            codigo_postal = faker.address().zipCode(),
            rol = "USER",
            token = null
        )
        val usuarioPersistido = entityManager.persist(usuario)

        val mascotaPerdida = MascotaDesaparecida(
            nombre = faker.dog().name(),
            especie = "Perro",
            raza = faker.dog().breed(),
            edad = 3,
            color = faker.color().name(),
            descripcion = faker.lorem().sentence(),
            zonaDesaparicion = faker.address().streetName(),
            fechaDesaparicion = LocalDate.parse("2023-10-01"),
            telefonoContacto = faker.phoneNumber().cellPhone(),
            encontrada = false,
            usuario = usuarioPersistido
        )
        val mascotaEncontrada = MascotaDesaparecida(
            nombre = faker.dog().name(),
            especie = "Perro",
            raza = faker.dog().breed(),
            edad = 5,
            color = faker.color().name(),
            descripcion = faker.lorem().sentence(),
            zonaDesaparicion = faker.address().streetName(),
            fechaDesaparicion = LocalDate.parse("2023-10-01"),
            telefonoContacto = faker.phoneNumber().cellPhone(),
            encontrada = true,
            usuario = usuarioPersistido
        )
        entityManager.persist(mascotaPerdida)
        entityManager.persist(mascotaEncontrada)
        entityManager.flush()


        val resultados = mascotaRepository.findByEncontradaFalse()


        assertEquals(1, resultados.size)
        assertEquals(mascotaPerdida.nombre, resultados[0].nombre)
    }

    @Test
    fun findByZonaDesaparicionContainingIgnoreCase_RetornaCoincidencias_SinImportarMayusculas() {

        val usuario = UsuarioEntity(
            nombre = faker.name().fullName(),
            correo = faker.internet().emailAddress(),
            contrasena = faker.internet().password(),
            codigo_postal = faker.address().zipCode(),
            rol = "USER",
            token = null
        )
        val usuarioPersistido = entityManager.persist(usuario)

        val mascotaEnCentro = MascotaDesaparecida(
            nombre = faker.cat().name(),
            especie = "Gato",
            raza = faker.cat().breed(),
            edad = 2,
            color = faker.color().name(),
            descripcion = faker.lorem().sentence(),
            zonaDesaparicion = "Colonia Centro",
            fechaDesaparicion = LocalDate.parse("2023-10-01"),
            telefonoContacto = faker.phoneNumber().cellPhone(),
            encontrada = false,
            usuario = usuarioPersistido
        )
        entityManager.persist(mascotaEnCentro)
        entityManager.flush()


        val resultados = mascotaRepository.findByZonaDesaparicionContainingIgnoreCase("centro")


        assertEquals(1, resultados.size)
        assertEquals("Colonia Centro", resultados[0].zonaDesaparicion)
    }
}