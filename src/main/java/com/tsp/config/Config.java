package com.tsp.config;

import com.utils.JwtAuthenticationFilter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Classe de configuration de la sécurité pour l'application.
 * Cette classe configure les règles de sécurité, notamment :
 * - La désactivation du CSRF pour les API REST.
 * - La gestion des CORS pour permettre les requêtes provenant de clients externes (comme Angular).
 * - La définition des autorisations pour les différentes routes.
 */
@Configuration
public class Config {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Méthode appelée après l'initialisation des beans Spring.
     * Utile pour vérifier que les beans sont correctement configurés.
     */
    @PostConstruct
    public void init() {
        System.out.println("Beans initialized");
    }

    /**
     * Définit la chaîne de filtres de sécurité pour l'application.
     *
     * @param http L'objet HttpSecurity utilisé pour configurer les règles de sécurité.
     * @return Un bean de type SecurityFilterChain qui représente la configuration de sécurité.
     * @throws Exception En cas d'erreur lors de la configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuration de la sécurité HTTP
        http.csrf(AbstractHttpConfigurer::disable) // Désactive la protection CSRF (utile pour les API REST, mais à activer si nécessaire)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configure les règles de CORS
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll() // Permet l'accès sans authentification à toutes les routes sous /auth/
                        .requestMatchers("/api/users/**").hasAuthority("SUPERADMIN") // Restreint l'accès aux routes superadmin aux utilisateurs avec le rôle SUPERADMIN
                        .requestMatchers("/api/tasks/**").hasAuthority("MANAGER").requestMatchers("/api/logs/**").hasAuthority("MANAGER")// Restreint l'accès aux routes manager aux utilisateurs avec le rôle MANAGER
                        .anyRequest().authenticated() // Toutes les autres routes nécessitent une authentification
                )

                // Ajoute le filtre JWT avant le UsernamePasswordAuthenticationFilter de Spring Security
                .addFilterBefore(new JwtAuthenticationFilter(SECRET_KEY), UsernamePasswordAuthenticationFilter.class);


        return http.build(); // Génère la chaîne de filtres avec la configuration définie
    }

    /**
     * Configure les règles CORS (Cross-Origin Resource Sharing) pour permettre
     * les requêtes provenant de clients externes.
     *
     * @return Une instance de UrlBasedCorsConfigurationSource contenant les règles de CORS.
     */
    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        // Déclare une source de configuration CORS basée sur les URL
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Crée une configuration CORS
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Permet les requêtes avec cookies/credentials
        config.addAllowedOriginPattern("http://localhost:4200"); // Autorise les requêtes provenant de l'origine spécifiée (ex. client Angular)
        config.addAllowedHeader("*"); // Autorise tous les en-têtes HTTP
        config.addAllowedMethod("*"); // Autorise toutes les méthodes HTTP (GET, POST, PUT, DELETE, etc.)

        // Associe la configuration à toutes les routes
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Définit un bean de type {@link PasswordEncoder}.
     * Ce bean utilise l'algorithme BCrypt pour hacher et vérifier les mots de passe.
     * BCrypt est un algorithme de hachage robuste et sécurisé, adapté à la gestion des mots de passe.
     *
     * @return Une instance de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Retourne une instance de BCryptPasswordEncoder pour gérer les mots de passe hashés
        return new BCryptPasswordEncoder();
    }
}
