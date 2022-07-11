package com.tates.api.demo.comparators;

import com.tates.api.demo.interfaces.CommonMethods.CommonMethods;
import com.tates.api.demo.models.Family;
import com.tates.api.demo.models.Food;
import com.tates.api.demo.models.Language;
import com.tates.api.demo.models.User;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EntityNameComparator<T >  implements Comparator<T > {
    @Override
    public int compare(T t1, T t2) {
        if (t1 instanceof User) {
            return ((User) t1).getName().compareTo(((User) t2).getName());
        } else if (t1 instanceof Food) {
            return ((Food) t1).getName().compareTo(((Food) t2).getName());
        } else if (t1 instanceof  Language) {
            return ((Language) t1).getName().compareTo(((Language) t2).getName());
        }else {
            return ((Family) t1).getName().compareTo(((Family) t2).getName());
        }
    }
}
