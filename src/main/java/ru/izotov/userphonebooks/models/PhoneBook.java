package ru.izotov.userphonebooks.models;

import ru.izotov.userphonebooks.entities.PhoneBookEntity;

public class PhoneBook {

    public PhoneBook() {
    }

    private Long id;
    private String username;
    private String phonenumber;


    public static PhoneBook toModel(PhoneBookEntity entity){
        PhoneBook book = new PhoneBook();
        book.setId(entity.getId());
        book.setUsername(entity.getNumber().getUsername());
        book.setPhonenumber(entity.getNumber().getPhonenumber());
        return book;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
