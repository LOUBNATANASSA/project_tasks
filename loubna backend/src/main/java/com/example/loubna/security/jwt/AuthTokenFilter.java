package com.example.loubna.security.jwt;

import com.example.loubna.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                System.out.println("1. Token valide. Username extrait : " + username);

                // On charge l'utilisateur
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("2. Utilisateur trouvé en base : " + userDetails.getUsername());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("3. Authentification enregistrée dans le contexte de sécurité !");
            } else {
                System.out.println("0. Token null ou invalide");
            }
        } catch (Exception e) {
            // C'est ici qu'on verra si l'utilisateur n'existe pas
            System.out.println("ERREUR DANS LE FILTRE : " + e.getMessage());
            e.printStackTrace(); // Affiche toute l'erreur
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        // --- AJOUTEZ CETTE LIGNE POUR VOIR CE QUI SE PASSE ---
        System.out.println("HEADER REÇU : " + headerAuth);
        // -----------------------------------------------------

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}