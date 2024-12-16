package com.tsp.services;

import com.tsp.models.User;
import com.tsp.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service responsable de l'authentification et de la gestion des tokens JWT.
 * Vérifie les informations d'identification de l'utilisateur et génère un token JWT.
 */
@Service
public class AuthService {

    // Clé secrète utilisée pour signer et vérifier les tokens JWT, injectée depuis la configuration de l'application
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param userRepository Repository des utilisateurs pour accéder aux données
     * @param passwordEncoder Encodeur de mot de passe pour vérifier les mots de passe hashés
     */
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Vérifie les informations d'identification de l'utilisateur (nom d'utilisateur et mot de passe).
     * Si les informations sont valides, génère un token JWT contenant les informations utilisateur.
     *
     * @param username Nom d'utilisateur fourni
     * @param password Mot de passe fourni
     * @return Un token JWT si les informations sont valides
     * @throws IllegalArgumentException Si l'utilisateur n'existe pas ou si le mot de passe est invalide
     */
    public String login(String username, String password) {
        // Recherche l'utilisateur par nom d'utilisateur
        Optional<User> optionalUser = userRepository.findOne((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("username"), username));

        // Vérifie si l'utilisateur existe
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé.");
        }

        User user = optionalUser.get();

        // Vérifie si le mot de passe est correct
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe incorrect.");
        }

        // Détermine le rôle (dans cet exemple, tous les utilisateurs sont MANAGER sauf "superadmin")
        String role = username.equals("superadmin") ? "SUPERADMIN" : "MANAGER";

        // Génère et retourne un token JWT
        return generateToken(user.getUsername(), role);
    }

    /**
     * Génère un token JWT contenant les informations utilisateur et son rôle.
     *
     * @param username Nom d'utilisateur à inclure dans le token
     * @param role     Rôle de l'utilisateur (par exemple, SUPERADMIN ou MANAGER)
     * @return Un token JWT signé
     */
    private String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // Ajoute le rôle dans les claims

        return Jwts.builder()
                .setClaims(claims) // Ajoute les claims (données personnalisées)
                .setSubject(username) // Définit le "subject" (nom d'utilisateur)
                .setIssuedAt(new Date()) // Définit la date de création du token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiration : 10 heures
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Signe le token avec une clé secrète
                .compact(); // Génère le token
    }
}
