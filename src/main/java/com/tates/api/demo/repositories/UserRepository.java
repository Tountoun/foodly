package com.tates.api.demo.repositories;

import com.tates.api.demo.models.Language;
import com.tates.api.demo.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();
    User findById(Integer id);
    Boolean existsByEmail(String email);
    User save(User user);
    //List<User> saveAll(List<User> users);
    Boolean existsById(Integer id);
    // Rechercher un utilisateur à partir de son email
    User findByEmail(String email);
    List<User> findAllByLanguage(Language language);
}
