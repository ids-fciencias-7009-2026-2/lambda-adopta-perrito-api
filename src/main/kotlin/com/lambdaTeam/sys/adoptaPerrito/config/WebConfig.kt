package com.lambdaTeam.sys.adoptaPerrito.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // Obtenemos la ruta absoluta de la carpeta donde guardamos las fotos
        val rutaAbsoluta = Paths.get("uploads/mascotas").toAbsolutePath().toUri().toString()

        // Le decimos a Spring que las URLs /imagenes/... apuntan a esa carpeta física
        registry.addResourceHandler("/imagenes/**")
            .addResourceLocations(rutaAbsoluta)
    }
}

