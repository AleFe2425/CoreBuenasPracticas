package com.example.BackendCoreMuebles.Configuraciones;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  //Deshabilita CSRF para facilitar la API REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").permitAll() //Permite el acceso sin autenticación a login y registro
                        .requestMatchers("/asignacionEmpleado/**").permitAll()
                        .requestMatchers("/detallePedido/**").permitAll()
                        .requestMatchers("/cliente/**").permitAll()
                        .requestMatchers("/material/**").permitAll()  //Protege las rutas de contrato
                        .requestMatchers("/empleado/**").permitAll()
                        .requestMatchers("/mueble/**").permitAll()  //Protege las rutas de reporte
                        .requestMatchers("/pedido/**").permitAll()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  //Indica que no se manejarán sesiones de usario en el servidor
                .logout(logout ->logout.permitAll()) //Permite logout
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
