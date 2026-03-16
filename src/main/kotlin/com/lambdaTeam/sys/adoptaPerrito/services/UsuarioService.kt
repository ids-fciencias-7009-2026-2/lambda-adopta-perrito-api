package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.domain.Usuario
import com.lambdaTeam.sys.adoptaPerrito.domain.toUsuario
import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import com.lambdaTeam.sys.adoptaPerrito.repositories.UsuarioRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsuarioService {
    val logger = LoggerFactory.getLogger(UsuarioService::class.java)

    @Autowired
    lateinit var usuarioRepository: UsuarioRepository

    fun buscaUsuarios(): List<Usuario> {
        val todosLosUsuarios = usuarioRepository.findAll()
        logger.info("all users:  ${todosLosUsuarios}")
        val usuarios = todosLosUsuarios.map { usuarioEntity -> usuarioEntity.toUsuario() }

        return usuarios
    }
}