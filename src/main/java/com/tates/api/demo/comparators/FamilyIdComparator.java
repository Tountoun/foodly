package com.tates.api.demo.comparators;

import com.tates.api.demo.models.Family;

import java.util.Comparator;
public class FamilyIdComparator  implements Comparator<Family> {
    @Override
    public int compare(Family family1, Family family2) {
        return family1.getId() - family2.getId();
    }
}
