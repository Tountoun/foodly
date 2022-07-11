package com.tates.api.demo.repositories;

import com.tates.api.demo.models.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends CrudRepository<Food, Long> {
    List<Food> findAll();
    Food findById(Integer id);
    Food save(Food food);
    Food findByName(String name);
    Boolean existsByName(String name);
    Boolean existsById(Integer id);
    List<Food> findAllByName(String name);

}
