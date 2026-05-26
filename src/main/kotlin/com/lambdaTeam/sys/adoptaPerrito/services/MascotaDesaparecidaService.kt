package com.lambdaTeam.sys.adoptaPerrito.services

import com.lambdaTeam.sys.adoptaPerrito.dto.request.CrearMascotaDesaparecidaRequest
import com.lambdaTeam.sys.adoptaPerrito.dto.response.MascotaDesaparecidaResponseDTO

import com.lambdaTeam.sys.adoptaPerrito.entities.MascotaDesaparecida

import com.lambdaTeam.sys.adoptaPerrito.repositories.MascotaDesaparecidaRepository
import com.lambdaTeam.sys.adoptaPerrito.repositories.UsuarioRepository
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

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
        foto: MultipartFile?,
        usuarioId: Long
    ): MascotaDesaparecidaResponseDTO {

        val usuario = usuarioRepository
            .findById(usuarioId.toInt())
            .orElseThrow {
                RuntimeException(
                    "Usuario no encontrado"
                )
            }

        var rutaImagen: String? = null

        if (foto != null && !foto.isEmpty) {
            //nombre único para que no se dupliquen archivos con el mismo nombre
            val nombreUnico = UUID.randomUUID().toString() + "_" + foto.originalFilename
            val rutaCompleta = Paths.get("uploads/mascotas").resolve(nombreUnico).toAbsolutePath()

            // se guarda en uploads
            Files.copy(foto.inputStream, rutaCompleta)
            rutaImagen = "/imagenes/$nombreUnico"
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

            imagenUrl = rutaImagen,

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

        val mascota = repository
            .findById(id)
            .orElseThrow {
                RuntimeException(
                    "Mascota no encontrada"
                )
            }

        mascota.encontrada = true

        repository.save(mascota)
    }

    fun listarPorUsuario(usuarioId: Long): List<MascotaDesaparecidaResponseDTO> {
        return repository
            .findByUsuarioId(usuarioId.toInt())
            .map { convertirDTO(it) }
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
}