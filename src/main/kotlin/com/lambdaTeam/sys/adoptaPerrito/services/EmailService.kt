package com.lambdaTeam.sys.adoptaPerrito.services

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {

    fun enviarCorreo(correoDueno: String, nombreMascota: String, correoAdoptante: String) {
        val mensaje = SimpleMailMessage()

        mensaje.from = "tu_correo_de_gmail@gmail.com"
        mensaje.setTo(correoDueno)
        mensaje.setSubject("🐾 ¡Alguien quiere adoptar a $nombreMascota!")
        mensaje.setText(
            "¡Hola!\n\n" +
                    "Tenemos excelentes noticias. Un usuario está muy interesado en adoptar a tu mascota: $nombreMascota.\n\n" +
                    "El correo de contacto del interesado es: $correoAdoptante\n\n" +
                    "Por favor, ponte en contacto con esta persona a la brevedad para platicar sobre la adopción.\n\n" +
                    "¡Gracias por usar Adopta Perrito!"
        )

        mailSender.send(mensaje)
    }
}