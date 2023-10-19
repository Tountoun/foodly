package com.tates.api.demo.services;
/*
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

@Service
public class TokenAuthenticationService {
    private static final String HEADER_STRING = "Authorization";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private static final String SECRET = "lghfolaghoibgf";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());

    public  Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println(token);
        if (token != null) {
            try{
                JWTVerifier jwtVerifier = JWT.require(algorithm)
                      .build();
                DecodedJWT decodedJWT = jwtVerifier.verify(token);
                String user = decodedJWT.getSubject();
                System.out.println("Decoded: "+user+"  issuer: "+decodedJWT.getIssuer());
                return user != null ? new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()) : null;
            }catch (JWTVerificationException e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

}
*/