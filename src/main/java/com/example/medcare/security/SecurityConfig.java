package com.example.medcare.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Classe de configuração responsável por toda a camada de segurança da aplicação.
 *
 * <p>
 * Utiliza o Spring Security com autenticação baseada em JWT e sessões stateless.
 * Também define permissões de acesso aos endpoints, filtro personalizado, políticas
 * de CORS e configuração de criptografia de senha.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;
    /**
     * Configura a cadeia de filtros do Spring Security.
     *
     * <p>Funcionalidades principais:</p>
     * <ul>
     *     <li>Desativa CSRF por ser uma API stateless</li>
     *     <li>Define uso de JWT com política de sessão STATELESS</li>
     *     <li>Permite acesso a endpoints públicos como login e registro</li>
     *     <li>Exige autenticação para todas as outras rotas</li>
     *     <li>Adiciona o filtro JWT antes do filtro padrão de autenticação</li>
     * </ul>
     *
     * @param httpSecurity objeto de configuração do Spring Security
     * @return SecurityFilterChain configurada
     * @throws Exception em erros de configuração
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/signup").permitAll()
                .requestMatchers(HttpMethod.GET, "/auth/validate", "/auth/all").permitAll()
                .requestMatchers( "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers(HttpMethod.POST, "/person/create").permitAll()
                // .requestMatchers(HttpMethod.GET, "/auth/me").permitAll()
                                .requestMatchers("/medic/**").authenticated()
                                .requestMatchers("/patient/**").authenticated()
                .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

     /**
     * Configuração de CORS para permitir comunicação com frontends específicos.
     *
     * <p>Permite origens vindas de aplicações locais, comuns durante o desenvolvimento.</p>
     *
     * @return Configuração global de CORS aplicada a todos os endpoints
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:8085",
                "http://localhost:8082"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    /**
     * Bean responsável por fornecer o AuthenticationManager, necessário para
     * o processo de autenticação no 'login'.
     *
     * @param authenticationConfiguration configuração interna do Spring Boot
     * @return AuthenticationManager apto a autenticar usuários
     * @throws Exception caso o ‘manager’ não possa ser criado
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean responsável por criptografar senhas de usuários utilizando
     * o algoritmo BCrypt.
     *
     * @return Implementação de PasswordEncoder baseada em BCrypt
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
