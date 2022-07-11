package com.tates.api.demo.controllers;

import com.tates.api.demo.models.Admin;
import com.tates.api.demo.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Transactional
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> adminRegistration(
            @RequestBody Admin admin
    ){
        try{
             return this.adminService.registerAdmin(admin);
        }catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("Error", e.getMessage());
            return new ResponseEntity<>( map, HttpStatus.FORBIDDEN);
        }
    }
}
