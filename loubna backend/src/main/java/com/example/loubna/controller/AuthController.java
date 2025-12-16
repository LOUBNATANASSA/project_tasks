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

@CrossOrigin(origins = "*", maxAge = 3600) //pour angular
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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 5. On renvoie tout ça au Frontend
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erreur: Cet email est déjà utilisé !"));
        }

        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès !"));
    }
}