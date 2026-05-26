package com.lambdaTeam.sys.adoptaPerrito.repositories

import com.lambdaTeam.sys.adoptaPerrito.entities.MascotaDesaparecida
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MascotaDesaparecidaRepository :
    JpaRepository<MascotaDesaparecida, Long> {

    fun findByEncontradaFalse():
            List<MascotaDesaparecida>

    fun findByZonaDesaparicionContainingIgnoreCase(
        zona: String
    ): List<MascotaDesaparecida>

    @Query("SELECT m FROM MascotaDesaparecida m WHERE m.usuario.id_usuario = :usuarioId")
    fun findByUsuarioId(@Param("usuarioId") usuarioId: Int): List<MascotaDesaparecida>
}