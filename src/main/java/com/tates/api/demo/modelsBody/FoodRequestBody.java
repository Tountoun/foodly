package com.tates.api.demo.modelsBody;

import com.tates.api.demo.models.Family;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class FoodRequestBody {
    private Integer id;
    private String name;
    private String brand;
    private double sugar;
    private double calory;
    private double grease;
    private double protein;
    private Family family;
    private List<UserRequestBody> users;
    public FoodRequestBody(
            String name,
            String brand,
            double sugar,
            double calory,
            double grease,
            double protein,
            Family family){
        this.name = name;
        this.brand = brand;
        this.sugar = sugar;
        this.calory = calory;
        this.grease = grease;
        this.protein = protein;
        this.family = family;
    }

}
