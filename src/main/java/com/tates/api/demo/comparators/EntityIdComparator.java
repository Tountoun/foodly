package com.tates.api.demo.comparators;

import com.tates.api.demo.models.Family;
import com.tates.api.demo.models.Food;
import com.tates.api.demo.models.Language;
import com.tates.api.demo.models.User;

import java.util.Comparator;

public class EntityIdComparator<T> implements Comparator<T> {
    @Override
    public int compare(T t1, T t2) {
        if (t1 instanceof User) {
            return ((User) t1).getId() - (((User) t2).getId());
        } else if (t1 instanceof Food) {
            return ((Food) t1).getId() - (((Food) t2).getId());
        } else if (t1 instanceof Language) {
            return ((Language) t1).getId() - (((Language) t2).getId());
        }else {
            return ((Family) t1).getId() - (((Family) t2).getId());
        }
    }
}
