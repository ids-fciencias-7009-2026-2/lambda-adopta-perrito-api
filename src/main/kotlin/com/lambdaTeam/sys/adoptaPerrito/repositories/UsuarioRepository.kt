package com.lambdaTeam.sys.adoptaPerrito.repositories

import com.lambdaTeam.sys.adoptaPerrito.entities.UsuarioEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UsuarioRepository : CrudRepository<UsuarioEntity, Int> {

    @Query("select u from UsuarioEntity u where u.token = :token")
    fun findByToken(token: String): UsuarioEntity?

    @Query("select u from UsuarioEntity u where u.correo = :correo and u.contrasena = :contrasena")
    fun findUserByPasswordAndEmail(correo: String, contrasena: String): UsuarioEntity?

    @Modifying
    @Transactional
    @Query("update UsuarioEntity u set u.token = :token where u.id_usuario = :id")
    fun updateTokenById(id: Int, token: String?)


    fun findByCorreo(correo: String): UsuarioEntity?
}
