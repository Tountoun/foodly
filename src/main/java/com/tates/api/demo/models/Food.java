package com.tates.api.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "foods")
@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamicUpdate
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "food_id")
    private Integer id;
    @Column(name = "food_name", nullable = false)
    private String name;
    @Column(name ="brand", nullable = false)
    private String brand;
    @Column(name = "sugar")
    private double sugar;
    @Column(name = "calory")
    private double calory;
    @Column(name = "grease")
    private double grease;
    @Column(name = "protein")
    private double protein;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "fam_id"
    )
    private Family family;

    @ManyToMany(
            mappedBy = "foods",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Food(String name, String brand, double sugar, double caloric, double grease, double protein) {
        this.name = name;
        this.brand = brand;
        this.sugar = sugar;
        this.calory = caloric;
        this.grease = grease;
        this.protein = protein;
    }

    public void addUser(User user){
        this.users.add(user);
        user.getFoods().add(this);
    }
    public void removeUser(User user){
        this.users.remove(user);
        user.getFoods().remove(this);
    }

}
