package ru.izotov.userphonebooks.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.izotov.userphonebooks.entities.BookEntryEntity;
import ru.izotov.userphonebooks.entities.PhoneBookEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PhoneBook {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneBook.class);

    public PhoneBook() {
    }

    private Long id;
    private String owner;
    private List<BookEntry> entries;


    public static PhoneBook toModel(List<PhoneBookEntity> bookEntries) throws Exception {
        Long id = bookEntries.get(0).getOwner().getId();
        if(bookEntries.stream().allMatch(e -> e.getOwner().getId() == id)) {
            PhoneBook book = new PhoneBook();
            book.setId(id);
            book.setOwner(bookEntries.get(0).getOwner().getUserName());
            book.setEntries(
                    bookEntries.stream()
                            .map(bookEntity -> {
                                BookEntry ent = BookEntry.toModel(bookEntity.getEntry());
                                ent.setId(bookEntity.getId());
                                return ent;
                            }).collect(Collectors.toList()));
            return book;
        }
        else {
            LOGGER.warn("Полученные записи телефонной книги принадлежат разным пользователям");
            throw new Exception("The list contains multiple users");
        }
    }

    public static PhoneBook toModel(PhoneBookEntity bookEntity) {
        PhoneBook book = new PhoneBook();
        book.setId(bookEntity.getOwner().getId());
        book.setOwner(bookEntity.getOwner().getUserName());
        BookEntry entry = BookEntry.toModel(bookEntity.getEntry());
        entry.setId(book.getId());
        book.setEntries(Arrays.asList(entry));
        return book;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<BookEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<BookEntry> entries) {
        this.entries = entries;
    }
}
