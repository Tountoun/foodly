package com.tates.api.demo.comparators;

import com.tates.api.demo.models.Language;

import java.util.Comparator;

public class LanguageIdComparator implements Comparator<Language> {
    @Override
    public int compare(Language language1, Language language2) {
        return language1.getId() - language2.getId();
    }
}
