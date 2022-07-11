package com.tates.api.demo.repositories;

import com.tates.api.demo.models.Language;
import com.tates.api.demo.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long> {
    List<Language> findAll();
    Language findById(Integer id);
    Language save(Language language);
    //List<Language> saveAll(List<Language> languages);
    void deleteById(Integer id);
    Boolean existsByName(String languageName);
    Boolean existsById(Integer id);
    Language findByName(String name);

}
