package com.tsp.controllers;

import com.tsp.dtos.LoginRequestDTO;
import com.tsp.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur pour gérer l'authentification des utilisateurs.
 * Fournit une API de connexion pour obtenir un token JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructeur pour injecter le service d'authentification.
     *
     * @param authService Service d'authentification
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * API de connexion.
     * Permet aux utilisateurs de s'authentifier en envoyant leurs informations
     * d'identification dans le body de la requête et de recevoir un token JWT s'ils sont valides.
     *
     * @param loginRequest Objet contenant le nom d'utilisateur et le mot de passe
     * @return Un token JWT en cas de succès ou une erreur en cas d'échec
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // Appelle le service pour effectuer l'authentification et générer un token
            String token = authService.login(loginRequest.username(), loginRequest.password());

            // Retourne une réponse HTTP avec le token
            return ResponseEntity.ok(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            // Retourne une réponse HTTP 401 Unauthorized en cas d'erreur
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
