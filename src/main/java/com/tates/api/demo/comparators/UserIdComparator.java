package com.tates.api.demo.comparators;

import com.tates.api.demo.models.User;

import java.util.Comparator;

public class UserIdComparator implements Comparator<User> {
    @Override
    public int compare(User user1, User user2) {
        return user1.getId() - user2.getId();
    }
}
