package com.lambdaTeam.sys.adoptaPerrito.repositories

import com.lambdaTeam.sys.adoptaPerrito.entities.MascotaDesaparecida

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
}