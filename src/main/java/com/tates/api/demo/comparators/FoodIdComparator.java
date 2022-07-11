package com.tates.api.demo.comparators;

import com.tates.api.demo.models.Food;

import java.util.Comparator;

public class FoodIdComparator implements Comparator<Food> {
    @Override
    public int compare(Food food1, Food food2) {
        return food1.getId() - food2.getId();
    }
}
