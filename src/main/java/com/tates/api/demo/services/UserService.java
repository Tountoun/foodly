package com.tates.api.demo.services;

import com.tates.api.demo.comparators.EntityNameComparator;
import com.tates.api.demo.comparators.FoodIdComparator;
import com.tates.api.demo.comparators.UserIdComparator;
import com.tates.api.demo.models.Food;
import com.tates.api.demo.models.Language;
import com.tates.api.demo.models.User;
import com.tates.api.demo.modelsBody.UserRequestBody;
import com.tates.api.demo.repositories.FoodRepository;
import com.tates.api.demo.repositories.LanguageRepository;
import com.tates.api.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private FoodService foodService;
    @Autowired
    private final FoodRepository foodRepository;
    private final UserIdComparator userIdComparator;

    public UserService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
        this.userIdComparator = new UserIdComparator();
    }

    // Lecture de données
    // Recuperer tous les utilisateurs de la bd
    public List<User> getUsers(Optional<Boolean> sortByName){
        List<User> users = userRepository.findAll();
        if(sortByName.isPresent() && sortByName.get() == true){
                Collections.sort(users, new EntityNameComparator<User>());
        }else{
            Collections.sort(users, this.userIdComparator);
        }
        return users;
    }
    // Recuperer un utilisateur à partir de son id
    public User getUserById(Integer id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User with id = "+ id +" not found");
        }
        return userRepository.findById(id);
    }
    public User getUserByEmail(String email){
        if(!userRepository.existsByEmail(email)){
            throw new EntityNotFoundException("User with this email " + email +" not found");
        }
        return userRepository.findByEmail(email);
    }
    public Language getUserLanguage(Integer userId){
        if(!userRepository.existsById(userId)){
            throw new EntityNotFoundException("User with id "+ userId + " not found");
        }
        return userRepository.findById(userId).getLanguage();
    }
    public List<Food> getUserFoods(Integer userId){
        if(!userRepository.existsById(userId)){
            throw new EntityNotFoundException("User with id "+ userId + " not found");
        }
        User user = userRepository.findById(userId);
        return user.getFoods();
    }

    // Creation de données
    public User addUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new EntityExistsException("User with this email "+ user.getEmail() +" is already present in the database");
        }
        User user1 = userRepository.save(user);
        return user1;
    }
    public User saveUser(UserRequestBody userRequestBody){
        if(userRepository.existsByEmail(userRequestBody.getEmail())){
            throw new EntityExistsException("User with email "+ userRequestBody.getEmail()+ " is already present");
        }
        User user = new User(
                userRequestBody.getName(),
                userRequestBody.getEmail()
        );
        Language language = languageService.getLanguage(userRequestBody.getLanguage().getId());
        // Si l'utilisateur a une liste d'aliments, on ajoute l'utilisateur à la liste des users de ces aliments
        if(!userRequestBody.getFoods().isEmpty()){
            List<Integer> ids = new ArrayList<>();
            userRequestBody.getFoods().forEach(
                    food -> {
                        ids.add(food.getId());
                    }
            );
            List<Food> foods =  foodService.getFoodsById(ids);
            foods.forEach(food -> {
                food.addUser(user);
            });
        }
        language.addUser(user);
        return userRepository.save(user);
        //return userRepository.findByEmail(user.getEmail());
    }
    public List<User> addUsers(List<User> users){
        List<User> newUsers = users.stream().filter(
                user -> {
                    return !userRepository.existsByEmail(user.getEmail());
                }
        ).collect(Collectors.toList());;
        /*
        users.forEach(
                user -> {
                    if(!userRepository.existsByEmail(user.getEmail())){
                        newUsers.add(user);
                    }
                }
        );*/

        return (List<User>) userRepository.saveAll(newUsers);
    }
    // Suppression de données
    public void deleteUserById(Integer id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User with id " + id +" not found");
        }
        userRepository.deleteById(Long.valueOf(id));
    }

    public User updateUser(UserRequestBody userRequestBody){
        if(!userRepository.existsById(userRequestBody.getId())){
            throw new EntityNotFoundException("User with id "+ userRequestBody.getId() + " not found");
        }

        User userWithSameId = userRepository.findById(userRequestBody.getId());
        if(userRequestBody.getName() != null && !userRequestBody.getName().equals(userWithSameId.getName())){
            userWithSameId.setName(userRequestBody.getName());
        }
        if(userRequestBody.getEmail() != null && !userRequestBody.getEmail().equals(userWithSameId.getEmail())){
            userWithSameId.setEmail(userRequestBody.getEmail());
        }

        // Mise à jour de la langue de l'user
        if(userRequestBody.getLanguage() != null &&
                languageRepository.existsById(userRequestBody.getLanguage().getId()) &&
                userWithSameId.getLanguage().getId() != userRequestBody.getLanguage().getId()
        ){
            Language language = languageRepository.findById(userRequestBody.getLanguage().getId());
            userWithSameId.setLanguage(language);
            language.addUser(userWithSameId);
        }

        // Mise à jour des alliments de l'user
        if(userRequestBody.getFoods() != null || !userRequestBody.getFoods().isEmpty()){
            List<Integer>  validIds = new ArrayList<>(); // List of ids valid in the request body
            userRequestBody.getFoods()
                    .stream()
                    .filter(
                            food -> {
                                return foodRepository.existsById(food.getId());
                            }
                    )   // Filter the list of foods to select those that really exists in the db
                    .forEach(
                            food -> {
                                validIds.add(food.getId());
                            }
                    ); // Select only the ids of the filtered foods
            // Compare valid ids list with the initial ids of the user
            List<Integer> initialIds = new ArrayList<>();
            userWithSameId.getFoods()
                    .forEach(food -> {
                        initialIds.add(food.getId());
                    });
            initialIds.forEach(System.out::println);
            validIds.forEach(
                    integer -> {
                        if(!initialIds.contains(integer)){
                            Food food = foodRepository.findById(integer);
                            food.addUser(userWithSameId);
                        }
                    }
            );
        }

        return userRepository.save(userWithSameId);
    }
    // Mise à jour de données
      /*
    public User updateUser(User user){
        if(!userRepository.existsById(user.getId())){
            throw new EntityNotFoundException("User with id "+ user.getId()+" not found");
        }
        User oldUser = userRepository.findById(user.getId());
        if(!oldUser.getName().equals(user.getName())){
            oldUser.setName(user.getName());
        }
        if(!oldUser.getEmail().equals(user.getEmail())){
            oldUser.setEmail(user.getEmail());
        }
        return userRepository.save(oldUser);
    }

    public void deleteUsers(){
        userRepository.deleteAll();
    }
    public void deleteUsersById(Iterable<Long> ids){
        userRepository.deleteAllById(ids);
    }
    public void deleteUsers(Iterable<User> users){
        userRepository.deleteAll(users);
    }

    public Iterable<User> updateUsers(Iterable<User> users){
        return userRepository.saveAll(users);
    }
   */
}
