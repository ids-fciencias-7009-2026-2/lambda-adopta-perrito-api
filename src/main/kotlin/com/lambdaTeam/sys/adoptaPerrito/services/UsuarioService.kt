package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.domain.Usuario
import com.lambdaTeam.sys.adoptaPerrito.domain.toUsuario
import com.lambdaTeam.sys.adoptaPerrito.entities.toUsuarioEntity
import com.lambdaTeam.sys.adoptaPerrito.repositories.UsuarioRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UsuarioService {
    val logger: Logger = LoggerFactory.getLogger(UsuarioService::class.java)

    @Autowired
    lateinit var usuarioRepository: UsuarioRepository


    fun addNuevoUsuario(usuario: Usuario): Usuario {
        val usuarioEntity = usuario.toUsuarioEntity()
        val guardado = usuarioRepository.save(usuarioEntity)

        return guardado.toUsuario().apply {
            password = null
        }
    }


    fun login(correo: String, contrasenaHasheada: String): String? {
        val usuario = usuarioRepository.findUserByPasswordAndEmail(correo, contrasenaHasheada)
        val id = usuario?.id_usuario

        return if (id != null) {
            val token = UUID.randomUUID().toString()
            usuarioRepository.updateTokenById(id, token)
            logger.info("Login exitoso. Token generado para el usuario ID: $id")
            token
        } else {
            logger.warn("Intento de login fallido para el correo: $correo")
            null
        }
    }

    fun obtenerUsuarioPorToken(token: String): Usuario? {
        val entity = usuarioRepository.findByToken(token)
        return entity?.toUsuario()?.apply {
            password = null
        }
    }

    fun logout(token: String): Boolean {
        val entity = usuarioRepository.findByToken(token)
        val id = entity?.id_usuario

        return if (id != null) {
            usuarioRepository.updateTokenById(id, null)
            logger.info("Logout exitoso. Token invalidado para el usuario ID: $id")
            true
        } else {
            false
        }
    }

    fun buscaUsuarios(): List<Usuario> {
        val todosLosUsuarios = usuarioRepository.findAll()
        logger.info("Total de usuarios encontrados: ${todosLosUsuarios.count()}")

        return todosLosUsuarios.map { usuarioEntity ->
            usuarioEntity.toUsuario().apply {
                password = null // Por seguridad, nunca devolvemos el password
            }
        }
    }


    fun buscaPorId(id: Int): Usuario? {
        val entity = usuarioRepository.findById(id).orElse(null)
        return entity?.toUsuario()?.apply {
            password = null // Por seguridad, nunca devolvemos el password
        }
    }
}