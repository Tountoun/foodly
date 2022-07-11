package com.tates.api.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tates.api.demo.interfaces.CommonMethods.CommonMethods;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "families")
@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamicUpdate
public class Family implements CommonMethods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fam_id")
    private Integer id;
    @Column(name = "fam_name", nullable = false)
    private String name;

    // A une seule famille peuvent être associés  plusiseurs aliments
    @OneToMany(
            mappedBy = "family",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Food> foods = new ArrayList<>();

    public Family(String name){
        this.name = name;
    }

    public void addFood(Food food){
        foods.add(food);
        food.setFamily(this);
    }

    public void removeFood(Food food){
        foods.remove(food);
        food.setFamily(null);
    }

}
