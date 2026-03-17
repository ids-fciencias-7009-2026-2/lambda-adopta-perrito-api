package com.lambdaTeam.sys.adoptaPerrito.repositories

import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : CrudRepository<UsuarioEntity, Int> {

    @Query("select u from UsuarioEntity u where u.correo = :correo and u.contrasena = :contrasena")
    fun findUserByPasswordAndEmail(correo: String, contrasena: String): UsuarioEntity?
}
