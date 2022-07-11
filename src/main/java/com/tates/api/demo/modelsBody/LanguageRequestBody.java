package com.tates.api.demo.modelsBody;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageRequestBody {
    private String name;
    private String countryCode;

    public LanguageRequestBody(String name, String countryCode){
        this.name = name;
        this.countryCode = countryCode;
    }
}
