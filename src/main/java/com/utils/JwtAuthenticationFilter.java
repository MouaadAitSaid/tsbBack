package com.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtre personnalisé pour valider les tokens JWT dans les requêtes HTTP.
 * Ce filtre s'exécute une fois par requête et extrait les informations du token JWT,
 * comme le nom d'utilisateur et le rôle, pour les placer dans le contexte de sécurité Spring Security.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String SECRET_KEY;

    public JwtAuthenticationFilter(String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    /**
     * Méthode principale du filtre. Elle est exécutée pour chaque requête HTTP.
     * Elle valide le token JWT, extrait les informations nécessaires, et configure
     * le contexte de sécurité si le token est valide.
     *
     * @param request     La requête HTTP entrante
     * @param response    La réponse HTTP sortante
     * @param filterChain La chaîne de filtres (pour passer au filtre suivant après l'exécution de ce filtre)
     * @throws ServletException En cas de problème avec le traitement de la requête
     * @throws IOException      En cas d'erreur d'entrée/sortie
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            try {
                // Parse the JWT
                Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

                // Extract username and role from the token
                String username = claims.getSubject();
                String role = claims.get("role", String.class);

                // Create authorities based on the role
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                // Set the authentication object in the SecurityContext
                PreAuthenticatedAuthenticationToken authentication =
                        new PreAuthenticatedAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }

        // Continue with the next filter
        filterChain.doFilter(request, response);
    }}
