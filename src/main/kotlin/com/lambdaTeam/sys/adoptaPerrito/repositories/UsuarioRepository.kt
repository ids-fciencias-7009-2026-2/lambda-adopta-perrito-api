package com.lambdaTeam.sys.adoptaPerrito.repositories

import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import org.springframework.data.repository.CrudRepository

interface UsuarioRepository : CrudRepository<UsuarioEntity, Int> {
}