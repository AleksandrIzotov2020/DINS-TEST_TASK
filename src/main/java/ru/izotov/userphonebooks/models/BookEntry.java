package ru.izotov.userphonebooks.models;


import ru.izotov.userphonebooks.entities.BookEntryEntity;

public class BookEntry {

    public BookEntry() {
    }

    private Long id;
    private String userName;
    private String phoneNumber;

    public static BookEntry toModel(BookEntryEntity numberEntity){
        BookEntry model = new BookEntry();
        model.setId(numberEntity.getId());
        model.setUserName(numberEntity.getUserName());
        model.setPhoneNumber(numberEntity.getPhoneNumber());
        return model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
