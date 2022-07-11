package com.tates.api.demo.controllers;

import com.tates.api.demo.models.Family;
import com.tates.api.demo.services.FamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/family")
public class FamilyController {

    @Autowired
    FamilyService familyService;

    @GetMapping
    public List<Family> getFamilies(
            @RequestParam(name = "sortbyname", required = false,  defaultValue = "false")Optional<Boolean> sortedByName
            ){
        return familyService.getFamilies(sortedByName);
    }
    @PostMapping
    public ResponseEntity<Object> postFamilies(
            @RequestBody List<Family> families
    ){
        try {
            System.out.println("Ok");
            return ResponseEntity.ok().body(familyService.addFamilies(families));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getFamilyById(@PathVariable(value = "id") Integer id){
        try{
            return ResponseEntity.ok().body(familyService.getFamily(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/id")
    public ResponseEntity<Object> postFamily(
            @RequestBody Family family
            ){
        try {
            System.out.println("Ok");
            return ResponseEntity.ok().body(familyService.addFamily(family));
        }catch (EntityExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Object> getFamilyByName(@PathVariable(value = "name") String name){
        try{
            return ResponseEntity.ok().body(familyService.getFamilyByName(name));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/id")
    public ResponseEntity<Object> updateFamily(@RequestBody Family family){
        try{
            return ResponseEntity.ok(familyService.updateFamily(family));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
