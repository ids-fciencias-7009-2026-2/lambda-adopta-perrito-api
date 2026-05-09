package com.lambdaTeam.sys.adoptaPerrito.repositories

import com.lambdaTeam.sys.adoptaPerrito.entities.AnimalEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AnimalRepository : CrudRepository<AnimalEntity, Int> {

    @Query("""
        select a from AnimalEntity a 
        where (:especie is null or a.especie = :especie) 
        and (:raza is null or a.raza = :raza) 
        and (:cp is null or a.codigo_postal = :cp)
    """)
    fun findByFilters(
        @Param("especie") especie: String?,
        @Param("raza") raza: String?,
        @Param("cp") cp: String?
    ): List<AnimalEntity>
}