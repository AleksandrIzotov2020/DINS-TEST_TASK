package ru.izotov.userphonebooks.entities;

import javax.persistence.*;

@Entity
@Table(name = "PhoneBook")
public class PhoneBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "phone_number_id")
    private PhoneNumberEntity number;

    public PhoneBookEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PhoneNumberEntity getNumber() {
        return number;
    }

    public void setNumber(PhoneNumberEntity number) {
        this.number = number;
    }
}
