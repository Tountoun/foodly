package com.tates.api.demo.services;

import com.tates.api.demo.Constants;
import com.tates.api.demo.models.Admin;
import com.tates.api.demo.repositories.AdminRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.security.Signature;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {
    private  final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    public ResponseEntity<Map<String, String>> registerAdmin(Admin admin){
        boolean exists = this.adminRepository.existsByEmail(admin.getEmail());
        if(!exists){
            return new ResponseEntity<>(generateJWTToken(admin), HttpStatus.OK);
        }
        throw new EntityExistsException("Admin with this email already exists");
    }
    // Methode permettant de generer un token à partir des données de l'admin
    public Map<String, String> generateJWTToken(Admin admin){
        long currentTime = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.JWT_SECRET_KEY)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + Constants.TOKEN_VALIDATY))
                .claim("adminId", admin.getId())
                .claim("adminName", admin.getName())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
