package com.tates.api.demo.models;

public class Admin {
    private Integer id;
    private String name;
    private String password;
    private String email;
    public Admin(String name, String password, String email){
        this.name = name;
        this.password = password;
        this.email = email;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
