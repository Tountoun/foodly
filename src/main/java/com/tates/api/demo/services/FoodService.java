package com.tates.api.demo.services;

import com.tates.api.demo.comparators.EntityIdComparator;
import com.tates.api.demo.comparators.EntityNameComparator;
import com.tates.api.demo.comparators.FoodIdComparator;
import com.tates.api.demo.models.Family;
import com.tates.api.demo.models.Food;
import com.tates.api.demo.models.User;
import com.tates.api.demo.modelsBody.FoodRequestBody;
import com.tates.api.demo.repositories.FamilyRepository;
import com.tates.api.demo.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class FoodService {

    @Autowired
    private final FoodRepository foodRepository;
    @Autowired
    private final FamilyRepository familyRepository;
    private final FoodIdComparator foodIdComparator;

    public FoodService(FoodRepository foodRepository, FamilyRepository familyRepository) {
        this.foodRepository = foodRepository;
        this.familyRepository = familyRepository;
        this.foodIdComparator = new FoodIdComparator();
    }

    // Lecture de données
    // Recuperer tous les aliments de la bd
    public List<Food> getFoods(Optional<Boolean> sortedByName){
        List<Food> foods = foodRepository.findAll();
        if(sortedByName.isPresent() && sortedByName.get() == true){
            Collections.sort(foods, new EntityNameComparator<Food>());
        }else{
            Collections.sort(foods, new EntityIdComparator<Food>());
        }
        return foods;
    }

    // Recuperer un aliment à partir de son id
    public Food getFood(Integer id){
        if(!foodRepository.existsById(id)){
            throw new EntityNotFoundException("Food with id "+ id + " not found");
        }
        Food food = foodRepository.findById(id);
        return food;
    }

    public List<Food> getFoodByName(String name){
        if(!foodRepository.existsByName(name)){
           throw new EntityNotFoundException("Food with name "+ name +" not found");
        }
        return foodRepository.findAllByName(name);
    }
    public List<Food> getFoodsById(List<Integer> ids){
        List<Food> foods = new ArrayList<>();
        ids.forEach(
                integer -> {
                    if(foodRepository.existsById(integer)){
                        foods.add(foodRepository.findById(integer));
                    }
                }
        );
        return foods;
    }
    // Creation de données

    public Food saveFood(FoodRequestBody foodRequestBody){
        if(foodRepository.existsByName(foodRequestBody.getName())){
            Food food = foodRepository.findByName(foodRequestBody.getName());
           if(Objects.equals(food.getBrand(), foodRequestBody.getBrand())){
               throw new EntityExistsException("Food with name "+ food.getName() +
                       " and brand "+ food.getBrand() + " is already present in db");
           }
        }
        Food food = new Food(
                foodRequestBody.getName(),
                foodRequestBody.getBrand(),
                foodRequestBody.getSugar(),
                foodRequestBody.getCalory(),
                foodRequestBody.getGrease(),
                foodRequestBody.getProtein()
        );
        Family family = familyRepository.findById(foodRequestBody.getFamily().getId());
        family.addFood(food);

        System.out.println("Food created successfully1");
        return foodRepository.save(food);
    }

    public List<User> getFoodUsers(Integer foodId){
        if(!foodRepository.existsById(foodId)){
            throw new EntityNotFoundException("Food with id "+ foodId + " not found ");
        }
        Food food = foodRepository.findById(foodId);
        return food.getUsers();
    }
    public Family getFoodFamily(Integer foodId){
        if(!foodRepository.existsById(foodId)){
            throw new EntityNotFoundException("Food with id "+ foodId + " not found");
        }
        return foodRepository.findById(foodId).getFamily();
    }

    public List<Food> addFoods(List<Food> foods){
        // nous devons eviter la duplication dans la base de données. Pour cela, on ne sauvegardera plus les eléments déjà presents dans la bd
        // Les clés sont true si la valeur food associé n'est pas presente en bd
        HashMap<Boolean, Food> hmp = new HashMap<>();
        foods.forEach(
                food -> {
                    if(foodRepository.existsByName(food.getName())){
                        hmp.put(false, food);
                    }else{
                        hmp.put(true, food);
                    }
                }
        );
        List<Food> returnList = new ArrayList<>();
        for (Map.Entry<Boolean, Food> mpEnt : hmp.entrySet()){
            if (mpEnt.getKey()){
                returnList.add(foodRepository.save(mpEnt.getValue()));
            }
        }

        return returnList;
    }

    public Food updateFood(FoodRequestBody foodRequestBody){
        if(!foodRepository.existsById(foodRequestBody.getId())){
            throw new EntityNotFoundException("Food with id "+ foodRequestBody.getId() + " not found");
        }
        Food withSameId = foodRepository.findById(foodRequestBody.getId());

        if(foodRequestBody.getName() != null &&
                !foodRequestBody.getName().equals(withSameId.getName())){
            withSameId.setName(foodRequestBody.getName());
        }
        if(foodRequestBody.getBrand() != null &&
                !foodRequestBody.getBrand().equals(withSameId.getBrand())){
            withSameId.setBrand(foodRequestBody.getBrand());
        }
        if(foodRequestBody.getSugar() != 0 &&
                foodRequestBody.getSugar() != withSameId.getSugar()){
            withSameId.setSugar(foodRequestBody.getSugar());
        }
        if(foodRequestBody.getCalory() != 0 &&
                foodRequestBody.getCalory() != withSameId.getCalory()){
            withSameId.setCalory(foodRequestBody.getCalory());
        }
        if(foodRequestBody.getGrease() != 0 &&
                foodRequestBody.getGrease() != withSameId.getGrease()){
            withSameId.setGrease(foodRequestBody.getGrease());
        }
        if(foodRequestBody.getProtein() != 0 &&
                foodRequestBody.getProtein() != withSameId.getProtein()){
            withSameId.setProtein(foodRequestBody.getProtein());
        }
        if(foodRequestBody.getFamily() != null &&
                foodRepository.existsById(foodRequestBody.getFamily().getId())
        ){
            Family family = familyRepository.findById(foodRequestBody.getFamily().getId());
            family.addFood(withSameId);
        }
        return foodRepository.save(withSameId);

    }

    // Suppression de données
    public void deleteFood(Food food){
        if(!foodRepository.existsByName(food.getName())){
            throw new EntityNotFoundException("Food with name " + food.getName()+" not found");
        }
        foodRepository.delete(food);
    }

    public void deleteFoodById(Integer id){
        if(!foodRepository.existsById(id)){
            throw new EntityNotFoundException("Food with id " + id + " not  found");
        }
        foodRepository.deleteById(Long.valueOf(id));
    }
    /*
    public void deleteFoods(){
        foodRepository.deleteAll();
    }


    public void deleteFoodsById(List<Long> ids){
        foodRepository.deleteAllById(ids);
    }
    public void deleteFoods(List<Food> foods){
        foodRepository.deleteAll(foods);
    }
     */
    // Mise à jour de données
    public Food updateFood(Food food){
        if(!foodRepository.existsById(food.getId())){
            throw new EntityNotFoundException("Food with id "+food.getId()+" not found");
        }
        Food oldFood = foodRepository.findById(food.getId());
        if(!food.getName().equals(oldFood.getName())){
            oldFood.setName(food.getName());
        }
        if(!oldFood.getBrand().equals(food.getBrand())){
            oldFood.setBrand(food.getBrand());
        }
        if(oldFood.getSugar() != food.getSugar()){
            oldFood.setSugar(food.getSugar());
        }
        if(oldFood.getCalory() != food.getCalory()){
            oldFood.setSugar(food.getCalory());
        }
        if(oldFood.getProtein() != food.getProtein()){
            oldFood.setSugar(food.getProtein());
        }
        return foodRepository.save(oldFood);
    }

    public List<User> getUsersByFood(FoodRequestBody foodRequestBody) {
        if(foodRepository.existsByName(foodRequestBody.getName())){
            List<Food> foods = foodRepository.findAllByName(foodRequestBody.getName());
            if(foods.size() > 1){
                Optional<Food> rightFood =  foods.stream()
                        .filter(food -> {return food.getBrand().compareToIgnoreCase(foodRequestBody.getBrand()) == 0;})
                        .findFirst();
                if(rightFood.isPresent()){
                    return rightFood.get().getUsers();
                }
                throw new EntityNotFoundException("Please, you must specify the right brand of "+ foodRequestBody.getName());
            }
            // Here the food's brand is unique
            // If the client set a brand, we check whether the food and the brand are coherent
            if(foodRequestBody.getBrand() == null || foods.get(0).getBrand().equals(foodRequestBody.getBrand())){
                return foods.get(0).getUsers();
            }
            throw new IllegalArgumentException("Food " + foodRequestBody.getName() + "' s brand (" + foods.get(0).getBrand() +") does not match with the one specified ("+ foodRequestBody.getBrand() +")");
        }
        throw  new EntityNotFoundException("Your request failed! Give the right food name and food brand");
    }

    public Family getFamilyByFood(FoodRequestBody foodRequestBody) {
        if(foodRepository.existsByName(foodRequestBody.getName())){
            List<Food> foods = foodRepository.findAllByName(foodRequestBody.getName());
            if(foods.size() > 1){
                Optional<Food> rightFood =  foods.stream()
                        .filter(food -> {return food.getBrand().compareToIgnoreCase(foodRequestBody.getBrand()) == 0;})
                        .findFirst();
                if(rightFood.isPresent()){
                    return rightFood.get().getFamily();
                }
                throw new EntityNotFoundException("Please, you must specify the right brand of "+ foodRequestBody.getName());
            }
            // Here the food's brand is unique
            // If the client set a brand, we check whether the food and the brand are coherent
            if(foodRequestBody.getBrand() == null || foods.get(0).getBrand().equals(foodRequestBody.getBrand())){
                return foods.get(0).getFamily();
            }
            throw new IllegalArgumentException("Food " + foodRequestBody.getName() + "' s brand (" + foods.get(0).getBrand() +") does not match with the one specified ("+ foodRequestBody.getBrand() +")");

        }
        throw  new EntityNotFoundException("Your request failed! Give the right food name and food brand");
    }
    /*
    public List<Food> updateFoods(List<Food> foods){
        return (List<Food>) foodRepository.saveAll(foods);
    }
     */

    }

