package com.tates.api.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "languages")
@Getter
@Setter
@ToString
@NoArgsConstructor
@DynamicUpdate
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lang_id")
    private Integer id;
    @Column(name = "lang_name", nullable = false)
    private String name;
    @Column(name = "country_code")
    private String countryCode;

    @OneToMany(
            mappedBy = "language",// l'attribut annnoté de la classe User
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Language(String name, String country_code){
        this.name = name;
        this.countryCode = country_code;
        this.users = new ArrayList<>();
    }

    public void addUser(User user){
        users.add(user);
        user.setLanguage(this);
    }
    public void removeUser(User user){
        users.remove(user);
        user.setLanguage(null);
    }
}

