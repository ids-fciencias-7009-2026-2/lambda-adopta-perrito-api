package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.dto.request.CrearMascotaDesaparecidaRequest
import com.lambdaTeam.sys.adoptaPerrito.dto.response.MascotaDesaparecidaResponseDTO

import com.lambdaTeam.sys.adoptaPerrito.entities.MascotaDesaparecida

import com.lambdaTeam.sys.adoptaPerrito.repositories.MascotaDesaparecidaRepository
import com.lambdaTeam.sys.adoptaPerrito.repositories.UsuarioRepository

import org.springframework.stereotype.Service

@Service
class MascotaDesaparecidaService(

    private val repository:
    MascotaDesaparecidaRepository,

    private val usuarioRepository:
    UsuarioRepository

) {

    fun crear(
        dto: CrearMascotaDesaparecidaRequest,
        usuarioId: Long
    ): MascotaDesaparecidaResponseDTO {

        val usuario = usuarioRepository
            .findById(usuarioId.toInt())
            .orElseThrow {
                RuntimeException(
                    "Usuario no encontrado"
                )
            }

        val mascota = MascotaDesaparecida(

            nombre = dto.nombre,

            especie = dto.especie,

            raza = dto.raza,

            edad = dto.edad,

            color = dto.color,

            descripcion = dto.descripcion,

            zonaDesaparicion =
                dto.zonaDesaparicion,

            fechaDesaparicion =
                dto.fechaDesaparicion,

            telefonoContacto =
                dto.telefonoContacto,

            imagenUrl = dto.imagenUrl,

            usuario = usuario
        )

        val guardada =
            repository.save(mascota)

        return convertirDTO(guardada)
    }

    fun listarActivas():
            List<MascotaDesaparecidaResponseDTO> {

        return repository
            .findByEncontradaFalse()
            .map { convertirDTO(it) }
    }

    fun obtenerPorId(
        id: Long
    ): MascotaDesaparecidaResponseDTO {

        val mascota = repository
            .findById(id)
            .orElseThrow {
                RuntimeException(
                    "Mascota no encontrada"
                )
            }

        return convertirDTO(mascota)
    }

    fun marcarComoEncontrada(id: Long) {
        val mascota = repository.findById(id).orElseThrow { RuntimeException("Mascota no encontrada") }

        mascota.encontrada = !mascota.encontrada

        repository.save(mascota)
    }

    fun buscarPorZona(
        zona: String
    ): List<MascotaDesaparecidaResponseDTO> {

        return repository
            .findByZonaDesaparicionContainingIgnoreCase(zona)
            .map { convertirDTO(it) }
    }

    private fun convertirDTO(
        mascota: MascotaDesaparecida
    ): MascotaDesaparecidaResponseDTO {

        return MascotaDesaparecidaResponseDTO(

            id = mascota.id,

            nombre = mascota.nombre,

            especie = mascota.especie,

            raza = mascota.raza,

            edad = mascota.edad,

            color = mascota.color,

            descripcion = mascota.descripcion,

            zonaDesaparicion =
                mascota.zonaDesaparicion,

            fechaDesaparicion =
                mascota.fechaDesaparicion,

            telefonoContacto =
                mascota.telefonoContacto,

            imagenUrl =
                mascota.imagenUrl,

            encontrada =
                mascota.encontrada
        )
    }

    fun obtenerMisMascotasPerdidas(idUsuario: Int): List<MascotaDesaparecidaResponseDTO> {
        return repository.findByUsuarioId(idUsuario).map { convertirDTO(it) }
    }
}