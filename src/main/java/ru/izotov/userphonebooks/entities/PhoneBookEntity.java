package ru.izotov.userphonebooks.entities;

import javax.persistence.*;

@Entity
@Table(name = "PhoneBook")
public class PhoneBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @ManyToOne
    @JoinColumn(name = "phone_number_id")
    private BookEntryEntity entry;

    public PhoneBookEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public BookEntryEntity getEntry() {
        return entry;
    }

    public void setEntry(BookEntryEntity entry) {
        this.entry = entry;
    }
}
