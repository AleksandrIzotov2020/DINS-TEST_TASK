package ru.izotov.userphonebooks.services;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.izotov.userphonebooks.entities.BookEntryEntity;
import ru.izotov.userphonebooks.entities.PhoneBookEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.models.BookEntry;
import ru.izotov.userphonebooks.repositories.PhoneBookRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PhoneBookServiceEditEntryTest {

    @Autowired
    private PhoneBookService bookService;
    @MockBean
    private PhoneBookRepo bookRepo;

    @Test
    public void editEntryTest() throws UserInteractionException {
        BookEntryEntity entry = new BookEntryEntity();
        entry.setUserName("Alex");
        entry.setPhoneNumber("79992223344");

        PhoneBookEntity book = new PhoneBookEntity();

        book.setEntry(new BookEntryEntity());
        book.getEntry().setId(1l);

        Mockito.doReturn(Optional.of(book))
                .when(bookRepo)
                .findById(1l);

        Assert.assertTrue(bookService.editEntry(entry, 1l));
        Assert.assertEquals("Alex", book.getEntry().getUserName());
        Assert.assertEquals("79992223344", book.getEntry().getPhoneNumber());

        Mockito.verify(bookRepo, Mockito.times(1)).save(book);
    }

    @Test
    public void editEntryFailTest() throws UserInteractionException {
        BookEntryEntity entry = new BookEntryEntity();
        Mockito.doReturn(Optional.ofNullable(null))
                .when(bookRepo)
                .findById(1l);

        Assert.assertFalse(bookService.editEntry(entry, 1l));
        Mockito.verify(bookRepo, Mockito.times(0)).save(new PhoneBookEntity());
    }

}