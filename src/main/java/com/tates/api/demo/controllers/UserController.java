package com.tates.api.demo.controllers;

import com.tates.api.demo.models.Admin;
import com.tates.api.demo.models.User;
import com.tates.api.demo.modelsBody.UserRequestBody;
import com.tates.api.demo.services.FoodService;
import com.tates.api.demo.services.LanguageService;
import com.tates.api.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    FoodService foodService;
    @Autowired
    LanguageService languageService;

    @GetMapping("")
    public List<User> getUsers(@RequestParam(name = "sortbyname", required = false, defaultValue = "false") Optional<Boolean> sortByName){
        return userService.getUsers(sortByName);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getUser(
            @PathVariable(value = "id") final Integer id
    ) {
        try{
            return ResponseEntity.ok().body(userService.getUserById(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<? extends Object> getUser(
            @PathVariable(value = "email") final String email
    ){
        try{
            return ResponseEntity.ok().body(userService.getUserByEmail(email));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
  /*  @GetMapping("/{id}/foods")
    public ResponseEntity<Object> getUserFoods(
            @PathVariable(value = "id") Integer id
    ){
        try{
            return ResponseEntity.ok().body(userService.getUserFoods(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}/language")
    public ResponseEntity<Object> getUserLanguage(
            @PathVariable(value = "id") Integer id
    ){
        try{
            return ResponseEntity.ok().body(userService.getUserLanguage(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }*/
    @PostMapping("/id")
    @Transactional
    public ResponseEntity<Object> postUser(
            @RequestBody UserRequestBody userRequestBody
    ){
        try{
            return ResponseEntity.ok().body(userService.saveUser(userRequestBody));
        }catch (EntityExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("")
    public ResponseEntity<?> postUsers(
            @RequestBody List<User> users
    )
    {
        try{
            return ResponseEntity.ok(userService.addUsers(users));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/id")
    public ResponseEntity<Object> updateUser(
            @RequestBody UserRequestBody userRequestBody
    ){
        try{
            return ResponseEntity.ok().body(userService.updateUser(userRequestBody));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("")
    public ResponseEntity<Object> updateUsers(
            @RequestBody List<UserRequestBody> userRequestBodyList
    ){
        try{
            return ResponseEntity.ok().body(userService.updateUsers(userRequestBodyList));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public String deleteUserById(
            @PathVariable(value = "id") Integer userId
    ){
        try{
            userService.deleteUserById(userId);
            return "Deleting successfully";
        }catch(Exception e){
            return e.getMessage();
        }
    }

}
