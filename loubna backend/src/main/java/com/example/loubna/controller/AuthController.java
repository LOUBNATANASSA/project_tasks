package com.example.loubna.controller;

import com.example.loubna.dto.request.LoginRequest;
import com.example.loubna.dto.request.SignupRequest;
import com.example.loubna.dto.response.JwtResponse;
import com.example.loubna.dto.response.MessageResponse;
import com.example.loubna.entity.User;
import com.example.loubna.repository.UserRepository;
import com.example.loubna.security.jwt.JwtUtils;
import com.example.loubna.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600) // Autorise React/Angular à nous appeler
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    // Endpoint pour se connecter (LOGIN)
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. On demande à Spring Security de vérifier le couple Email/Password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        // 2. Si c'est bon, on met l'info dans le contexte de sécurité
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. On génère le Token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 4. On récupère les infos de l'utilisateur connecté
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 5. On renvoie tout ça au Frontend
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(), // Note: ici getUsername() renvoie l'email
                userDetails.getEmail()));
    }

    // Endpoint pour s'inscrire (REGISTER)
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {

        // 1. On vérifie si l'email existe déjà
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erreur: Cet email est déjà utilisé !"));
        }

        // 2. On crée le nouvel utilisateur
        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())); // On crypte le mot de passe !

        // 3. On sauvegarde en base
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès !"));
    }
}