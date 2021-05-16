package ru.izotov.userphonebooks.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Users")
public class UserEntity {

    public UserEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @OneToOne(cascade =  CascadeType.ALL, mappedBy = "user") // При удлении пользователя cascade удаляет все его записи в телефонной книге
    private PhoneNumberEntity phoneNumberEntities;

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "user")
    private List<PhoneBookEntity> bookEntities;

    public List<PhoneBookEntity> getBookEntities() {
        return bookEntities;
    }

    public void setBookEntities(List<PhoneBookEntity> bookEntities) {
        this.bookEntities = bookEntities;
    }

    public PhoneNumberEntity getPhoneNumberEntities() {
        return phoneNumberEntities;
    }

    public void setPhoneNumberEntities(PhoneNumberEntity phoneNumberEntities) {
        this.phoneNumberEntities = phoneNumberEntities;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
