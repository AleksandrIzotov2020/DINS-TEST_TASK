package ru.izotov.userphonebooks.models;

import ru.izotov.userphonebooks.entities.PhoneNumberEntity;

public class PhoneNumber {

    public PhoneNumber() {
    }

    private Long id;
    private String username;
    private String phonenumber;

    public static PhoneNumber toModel(PhoneNumberEntity numberEntity){
        PhoneNumber model = new PhoneNumber();
        model.setId(numberEntity.getId());
        model.setUsername(numberEntity.getUsername());
        model.setPhonenumber(numberEntity.getPhonenumber());
        return model;
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

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
