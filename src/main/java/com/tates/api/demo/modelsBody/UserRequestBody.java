package com.tates.api.demo.modelsBody;

import com.tates.api.demo.models.Food;
import com.tates.api.demo.models.Language;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserRequestBody {

    private Integer id;
    private String name;
    private String email;
    private Language language;
    private List<Food> foods;
    public UserRequestBody(
            String name,
            String email,
            Language language,
            ArrayList<Food> foods){
        this.name = name;
        this.email = email;
        this.language = language;
        this.foods = foods;
    }

}
