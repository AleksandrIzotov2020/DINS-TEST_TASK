package ru.izotov.userphonebooks.models;

import ru.izotov.userphonebooks.entities.UserEntity;

public class User {

    public User() {
    }

    private Long id;
    private String username;

    public static User toModel(UserEntity userEntity){
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUserName());
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
