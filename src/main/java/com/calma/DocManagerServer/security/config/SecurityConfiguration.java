package com.calma.DocManagerServer.security.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfiguration
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher((matchers) -> Boolean.parseBoolean(matchers.getRequestId())) // Aplica a todas las solicitudes
                .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll()) // Permitir todo sin autenticación
                .csrf((csrf) -> csrf.disable()) // Desactivar CSRF si no es necesario
                .httpBasic(Customizer.withDefaults()); // Configuración adicional opcional
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
