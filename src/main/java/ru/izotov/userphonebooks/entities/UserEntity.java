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
    private String userName;
    private String password;

    @OneToOne(cascade =  CascadeType.ALL, mappedBy = "user") // При удлении пользователя cascade удаляет все его записи в телефонной книге
    private BookEntryEntity bookEntryEntity;

    @OneToMany(cascade =  CascadeType.ALL, mappedBy = "owner")
    private List<PhoneBookEntity> bookEntities;

    public List<PhoneBookEntity> getBookEntities() {
        return bookEntities;
    }

    public void setBookEntities(List<PhoneBookEntity> bookEntities) {
        this.bookEntities = bookEntities;
    }

    public BookEntryEntity getBookEntryEntity() {
        return bookEntryEntity;
    }

    public void setBookEntryEntity(BookEntryEntity bookEntryEntity) {
        this.bookEntryEntity = bookEntryEntity;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
