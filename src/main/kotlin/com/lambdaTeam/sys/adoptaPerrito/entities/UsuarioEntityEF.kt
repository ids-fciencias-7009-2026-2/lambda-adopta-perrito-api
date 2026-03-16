package com.lambdaTeam.sys.adoptaPerrito.entities

import com.lambdaTeam.sys.adoptaPerrito.domain.Usuario

fun Usuario.toUsuarioEntity(): UsuarioEntity {
    return UsuarioEntity(
        id_usuario = this.id,
        nombre = this.nombre,
        correo = this.email,
        codigo_postal = this.codigoPostal,
        contrasena = this.password ?: ""
    )
}