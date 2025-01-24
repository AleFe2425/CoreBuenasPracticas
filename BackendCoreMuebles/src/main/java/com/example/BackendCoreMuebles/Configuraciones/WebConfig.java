package com.example.BackendCoreMuebles.Configuraciones;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")  // Asegúrate de que coincida con tu ruta de controlador
                .allowedMethods("GET", "POST", "PUT", "DELETE")
        ;
    }
}
