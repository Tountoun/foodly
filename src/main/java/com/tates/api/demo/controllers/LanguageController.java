package com.tates.api.demo.controllers;

import com.tates.api.demo.exceptions.BadRequestException;
import com.tates.api.demo.models.Language;
import com.tates.api.demo.modelsBody.LanguageRequestBody;
import com.tates.api.demo.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/language")
@Transactional
public class LanguageController {
    @Autowired
    LanguageService languageService;
    @GetMapping
    public List<Language> getLanguages(
            @RequestParam(name = "sortbyname" , required = false, defaultValue = "false")Optional<Boolean> sortedByName
            ){
        return languageService.getLanguages(sortedByName);
    }
    @GetMapping("/{id}/users")
    public ResponseEntity<Object> getLanguageUsers(
            @PathVariable(value = "id") Integer languageId
    ){
        try{
            return ResponseEntity.ok(languageService.getUsersByLanguage(languageId));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/users")
    public ResponseEntity<Object> getUsersByLanguageNameAndCountryCode(
            @RequestBody LanguageRequestBody languageRequestBody
    ){
        try{
            return ResponseEntity.ok(languageService.getUsersByLanguage(languageRequestBody));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getLanguageById(
            @PathVariable(value = "id") Integer id
    ){
        try{
            return ResponseEntity.ok().body(languageService.getLanguage(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Object> getLanguageByName(
            @PathVariable(value = "name") String name
    ){
        try{
            return ResponseEntity.ok(languageService.getLanguageByName(name));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> postLanguages(
            @RequestBody List<Language> languages
    ){
        try{
            return ResponseEntity.ok(languageService.addLanguages(languages));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/id")
    public ResponseEntity<Object> postLanguage(
            @RequestBody Language language
    ){
        try{
            return ResponseEntity.ok().body(languageService.addLanguage(language));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/id")
    public ResponseEntity<Object> updateLanguage(
            @RequestBody Language language
            ){
        try{
            return ResponseEntity.ok().body(languageService.updateLanguage(language));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/ids")
    public ResponseEntity<Object> updateLanguages(
            @RequestBody List<Language> languages
    ){
        try{
            return ResponseEntity.ok().body(languageService.updateLanguages(languages));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public String deleteById(
            @PathVariable(value = "id") Integer id
    ){
        try{
            languageService.deleteLanguageById(id);
            return "Deleting successful";
        }catch (EntityNotFoundException e){
            return e.getMessage();
        }
    }
    @DeleteMapping
    public ResponseEntity<String> deleteLanguage(
            @RequestBody Language language
    ){
        try{
            languageService.deleteLanguage(language);
            return ResponseEntity.ok("Deleting successful");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/id")
    public String deleteById(
            @RequestParam(name="id", required = false) Optional<Integer> id
    ){
        try{
            if(id.isPresent()){
                languageService.deleteLanguageById(id.get());
                return "Language deleted successfully";
            }else{
                throw new BadRequestException("Bad request");
            }
        }catch (EntityNotFoundException e){
            return e.getMessage();
        }catch (BadRequestException e){
            return e.getMessage();
        }
    }

}
