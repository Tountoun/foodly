package com.tates.api.demo.repositories;

import com.tates.api.demo.models.Family;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyRepository extends CrudRepository<Family, Long> {
    // Requête personnalisée de type 'Derived Queries' pour recuperer une  famille par son nom
    List<Family> findAll();
    Family findById(Integer id);
    Boolean existsByName(String familyName);
    Family findByName(String name);
    Boolean existsById(Integer id);
    void deleteById(Integer id);
}
