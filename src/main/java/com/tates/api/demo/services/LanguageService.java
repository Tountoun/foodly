package com.tates.api.demo.services;

import com.tates.api.demo.comparators.EntityIdComparator;
import com.tates.api.demo.comparators.EntityNameComparator;
import com.tates.api.demo.comparators.LanguageIdComparator;
import com.tates.api.demo.exceptions.BadRequestException;
import com.tates.api.demo.exceptions.ResourceNotFoundException;
import com.tates.api.demo.models.Language;
import com.tates.api.demo.models.User;
import com.tates.api.demo.modelsBody.LanguageRequestBody;
import com.tates.api.demo.repositories.LanguageRepository;
import com.tates.api.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LanguageService {
    @Autowired
    private final LanguageRepository languageRepository;
    @Autowired
    private final UserRepository userRepository;
    private final LanguageIdComparator languageIdComparator;

    public LanguageService(LanguageRepository languageRepository, UserRepository userRepository) {
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
        this.languageIdComparator = new LanguageIdComparator();
    }

    // Lecture de données
    // Recuperer toutes les langues des utilisateurs
    public List<Language> getLanguages(Optional<Boolean> sortedByName){
        List<Language> languages = languageRepository.findAll();
        if(sortedByName.isPresent() && sortedByName.get() == true){
            Collections.sort(languages, new EntityNameComparator<Language>());
        }else{
            Collections.sort(languages, new EntityIdComparator<Language>());
        }
        return languages;
    }
    // Recuperer une langue à parir de son id
    public Language getLanguage(Integer id){
        if(!languageRepository.existsById(id)){
            throw new EntityNotFoundException("Language with id "+ id +" not found");
        }
        return languageRepository.findById(id);
    }
    public Language getLanguageByName(String name){
        if(!languageRepository.existsByName(name)){
            throw new EntityNotFoundException("Language with name "+ name + " not found");
        }
        return languageRepository.findByName(name);
    }
    public List<User> getUsersByLanguage(Integer languageId) {
        if(languageRepository.existsById(languageId)){
            Language language = languageRepository.findById(languageId);
            return userRepository.findAllByLanguage(language);
        }
        throw new ResourceNotFoundException("Language with id " + languageId +" not found");
        //throw new EntityNotFoundException("Language with id "+languageId + " does not exists");
    }
    public List<User> getUsersByLanguage(LanguageRequestBody languageRequestBody) {
        if(languageRepository.existsByName(languageRequestBody.getName())){
            Language language = languageRepository.findByName(languageRequestBody.getName());
            if(language.getCountryCode().compareToIgnoreCase(languageRequestBody.getCountryCode()) == 0){
                return userRepository.findAllByLanguage(language);
            }
            throw new BadRequestException("Language code specified does not exist");
            //throw new IllegalArgumentException("Language code specified does not exist");
        }
        throw new ResourceNotFoundException("Language with name "+languageRequestBody.getName() + " not found");
        //throw new EntityNotFoundException("Language with name "+languageRequestBody.getName() + " not found");
    }

    // Creation de données
    public Language addLanguage(Language language){
        if(languageRepository.existsByName(language.getName())){
            throw new EntityExistsException("Language with name "+ language.getName()+" already exists");
        }
        return languageRepository.save(language);
    }
    public List<Language> addLanguages(List<Language> languages){
        List<Language> languages1 = languages.stream()
                .filter(language -> {
                    return !languageRepository.existsByName(language.getName());
                })
                .collect(Collectors.toList());
        languages1.forEach(
                language -> {
                    languageRepository.save(language);
                }
        );
        return languages1;
    }

    /*
    public void deleteLanguages(){
        languageRepository.deleteAll();
    }
    public void deleteLanguagesById(Iterable<Long> ids){
        languageRepository.deleteAllById(ids);
    }
    public void deleteLanguages(Iterable<Language> languages){
        languageRepository.deleteAll(languages);
    }     */

    // Mise à jour de données
    public Language updateLanguage(Language language){
        if(!languageRepository.existsById(language.getId())){
            throw new ResourceNotFoundException("Language with id " + language.getId() + " not found");
            //throw new EntityNotFoundException("Language with id " + language.getId()+ " not found");
        }
        Language language1 = languageRepository.findById(language.getId());
        if(!language1.getName().equals(language.getName())
                && language.getName() != null){
            language1.setName(language.getName());
        }
        if(!language1.getCountryCode().equals(language.getCountryCode())
                && language.getCountryCode() != null){
            language1.setCountryCode(language.getCountryCode());
        }
        return languageRepository.save(language1);
    }
    public List<Language> updateLanguages(List<Language> languages){
        if(languages.size() > 0){
            List<Language> updated = new ArrayList<>();
            languages.forEach(
                    language -> {
                        updated.add(this.updateLanguage(language));
                    }
            );
            return updated;
        }
        return null;
    }

    // Suppression de données
    public void deleteLanguage(Language language){
        if(!languageRepository.existsByName(language.getName())){
            throw new ResourceNotFoundException("Language with name "+language.getName()+" not found");
            // throw new EntityNotFoundException("Language with name " + language.getName() +" not found");
        }
        Language sameId = languageRepository.findById(language.getId());
        if(!sameId.getName().equals(language.getName()) ||
                !sameId.getCountryCode().equals(language.getCountryCode())
        ){
            throw new BadRequestException("Valeurs non compatibles");
            // throw new InputMismatchException("Valeurs non compatibles");
        }
        languageRepository.delete(language);
        System.out.println("Delete successfully");
    }
    public void deleteLanguageById(Integer id){
        if(!languageRepository.existsById(id)){
            throw new ResourceNotFoundException("Language with id " + id+ " not found");
            //throw new EntityNotFoundException("Language with id " + id + " not found");
        }
        languageRepository.deleteById(id);
    }

}
