package ru.izotov.userphonebooks.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "BookEntry")
public class BookEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;
    private String userName;


    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "entry")
    private List<PhoneBookEntity> bookEntities;

    public BookEntryEntity() {
    }

    public List<PhoneBookEntity> getBookEntities() {
        return bookEntities;
    }

    public void setBookEntities(List<PhoneBookEntity> bookEntities) {
        this.bookEntities = bookEntities;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
