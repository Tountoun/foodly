package com.tates.api.demo.services;

import com.tates.api.demo.comparators.EntityIdComparator;
import com.tates.api.demo.comparators.EntityNameComparator;
import com.tates.api.demo.comparators.FamilyIdComparator;
import com.tates.api.demo.models.Family;
import com.tates.api.demo.repositories.FamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FamilyService {
    @Autowired
    private final FamilyRepository familyRepository;
    //private final FamilyIdComparator familyIdComparator;
    private final EntityIdComparator<Family> entityIdComparator;
    public FamilyService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
        this.entityIdComparator =  new EntityIdComparator<>();
        //this.familyIdComparator = new FamilyIdComparator();
    }

    // Lecture de données
    // Recuperer toutes les familles d'aliments disponibles
    public List<Family> getFamilies(Optional<Boolean> sortedByName){
        List<Family> families = familyRepository.findAll();
        if(sortedByName.isPresent() && sortedByName.get() == true){
            Collections.sort(families, new EntityNameComparator<Family>());
        }else{
            Collections.sort(families, this.entityIdComparator);
        }
        return families;
    }
    // Recuperer une famille d'aliments à partir de son id
    public Family getFamily(Integer id){
        if(!familyRepository.existsById(id)){
            throw new EntityNotFoundException("Family with id "+ id + " not found");
        }
        return familyRepository.findById(id);
    }
    public Family getFamilyByName(String name){
        if(!familyRepository.existsByName(name)){
            throw new EntityNotFoundException("Family with name " + name + " not found");
        }
        return familyRepository.findByName(name);
    }
    // Creation de données
    public Family addFamily(Family family){
        if(familyRepository.existsByName(family.getName())){
            throw new EntityExistsException("Family with name "+ family.getName()+" already exists");
        }
        return familyRepository.save(family);
    }
    public List<Family> addFamilies(List<Family> families){
        List<Family> families1 = families.stream()
                .filter(
                        family -> {
                            return !familyRepository.existsByName(family.getName());
                        }
                ).collect(Collectors.toList());
        return (List<Family>) familyRepository.saveAll(families1);
    }
    // Mise de données
    public Family updateFamily(Family family){
        if(!familyRepository.existsById(family.getId())){
            throw new EntityNotFoundException("Family with id " + family.getId() + " not found");
        }
        Family family1 = familyRepository.findById(family.getId());
        if(!family1.getName().equals(family.getName())){
            family1.setName(family.getName());
        }
        return familyRepository.save(family1);
    }

    public void deleteFamilyById(Integer id){
        if(!familyRepository.existsById(id)){
            throw new EntityNotFoundException("Family with id "+ id + " not found");
        }
        familyRepository.deleteById(id);
    }

    /*
    // Suppression de données
    public void deleteFamily(Family family){
        familyRepository.delete(family);
    }

    public void deleteFamilies(){
        familyRepository.deleteAll();
    }
    public void deleteFamiliesById(List<Long> ids){
        familyRepository.deleteAllById(ids);
    }
    public void deleteFamilies(List<Family> families){
        familyRepository.deleteAll(families);
    }

    public List<Family> updateFamilies(List<Family> families){
        return (List<Family>) familyRepository.saveAll(families);
    }


     */

}
