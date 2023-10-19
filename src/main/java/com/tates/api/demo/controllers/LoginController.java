package com.tates.api.demo.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("api/v1/login")
public class LoginController {
    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public LoginController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }
    @GetMapping("/refresh_token")
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Ici le client envoit un refresh token dans le but d'avoir un nouveau acces token
        String authorization = request.getHeader(AUTHORIZATION);
        if(authorization != null && authorization.startsWith("Bearer ")){
            try{
                String refresh_token = authorization.substring("Bearer ".length());//Suppression du mot Bearer
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                UserDetails userDetails = this.inMemoryUserDetailsManager.loadUserByUsername(username);
                // Creation d'un nouveau access token
                String access_token = JWT.create()
                        .withSubject(userDetails.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 *1000))
                        .withClaim("roles", userDetails.getAuthorities().stream().map(
                                grantedAuthority -> grantedAuthority.getAuthority()
                                /*GrantedAuthority::getAuthority*/
                        ).collect(Collectors.toList()))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("Access Token", access_token);
                tokens.put("Refresh Token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception e){
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
    }
    @PostMapping("create/{username}/{password}/{role}")
    public ResponseEntity<String> login(@PathVariable(name = "username") String username,
                                        @PathVariable(name = "password") String password){
        System.out.println("Hello");
        UserDetails userDetails = inMemoryUserDetailsManager.loadUserByUsername(username);
        System.out.println("ok");
        if(userDetails == null){
            this.inMemoryUserDetailsManager.createUser(User.withUsername(username).password(password).build());
            return ResponseEntity.created(URI.create("api/v1/login/create")).body("User created");
        }
        return null;
    }
}
