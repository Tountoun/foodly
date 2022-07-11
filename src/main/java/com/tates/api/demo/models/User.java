package com.tates.api.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@DynamicUpdate // Ameliore la performance des opérations de mise à jour
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer id;
    @Column(name = "user_name", unique = true, nullable = false)
    private String name;
    @Column(name = "email")
    private String email;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "lang_id") // La clé etrangère dans l'entité user
    @JsonIgnore
    private Language language;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "users_foods",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    @JsonIgnore
   private List<Food> foods = new ArrayList<>();

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Definition de methodes utilitaires
    // Synchronisation des deux entités associées
    // Ajout d'un aliment et la spécification de son user
    public void addFood(Food food){
        foods.add(food);
        food.getUsers().add(this);
    }
    // Suppession d'un aliment et son user
    public void removeFood(Food food){
        foods.remove(food);
        food.getUsers().remove(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", language=" + language.getName() +
                ", foods=" + foods +
                '}';
    }
}
