package com.tates.api.demo.controllers;

import com.tates.api.demo.models.Food;
import com.tates.api.demo.modelsBody.FoodRequestBody;
import com.tates.api.demo.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/food")
public class FoodController {
    @Autowired
    FoodService foodService;

    @GetMapping
    public List<Food> getFoods(
            @RequestParam(name = "sortbyname", required = false, defaultValue = "false")Optional<Boolean> sortedByName
            ){
            return foodService.getFoods(sortedByName);
    }
    @GetMapping("id/{id}")
    public ResponseEntity<Object> getFoodById(
            @PathVariable(value = "id") final Integer id
    ){
        try{
            return ResponseEntity.ok(foodService.getFood(id));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("name/{name}")
    public ResponseEntity<Object> getFoodByName(
            @PathVariable(value = "name") String name
    ){
        try{
            return ResponseEntity.ok().body(foodService.getFoodByName(name));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}/users")
    public ResponseEntity<Object> getFoodUsers(
            @PathVariable(value = "id") Integer id
    ){
        try{
            return ResponseEntity.ok().body(foodService.getFoodUsers(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/users")
    public ResponseEntity<Object> getUsersByFoodNameAndBrand(
            @RequestBody FoodRequestBody foodRequestBody
    ){
        try{
            return ResponseEntity.ok(foodService.getUsersByFood(foodRequestBody));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}/family")
    public ResponseEntity<Object> getFoodFamily(
            @PathVariable(value = "id") Integer id
    ){
        try {
            return ResponseEntity.ok().body(foodService.getFoodFamily(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/family")
    public ResponseEntity<Object> getFamilyByFoodNameAndBrand(
            @RequestBody FoodRequestBody foodRequestBody
    ){
        try{
            return ResponseEntity.ok(foodService.getFamilyByFood(foodRequestBody));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/id")
    public ResponseEntity<Object> postFood(
            @RequestBody FoodRequestBody foodRequestBody
            ){
        try{
            return ResponseEntity.ok().body(foodService.saveFood(foodRequestBody));
        }catch (EntityExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/id")
    public ResponseEntity<Object> updateFood(
            @RequestBody FoodRequestBody foodRequestBody
    ){
        try{
            return ResponseEntity.ok().body(foodService.updateFood(foodRequestBody));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public String deleteFoodById(
            @PathVariable(value = "id") Integer foodId
    ){
        try{
            foodService.deleteFoodById(foodId);
            return "Food deleted successfully";
        }catch(Exception e){
            return e.getMessage();
        }
    }
}
