package ru.izotov.userphonebooks.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PhoneNumber")
public class PhoneNumberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phonenumber;

    private String username;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "number")
    private List<PhoneBookEntity> bookEntities;

    public PhoneNumberEntity() {
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
