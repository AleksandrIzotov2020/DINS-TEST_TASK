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
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.repositories.UserRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class EditUserTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepo userRepo;

    @Test
    public void editUser() throws UserInteractionException {
        UserEntity user = new UserEntity();
        user.setUserName("Aleksandr");
        user.setPassword("1234456");

        UserEntity dbUser = new UserEntity();
        dbUser.setId(1l);
        dbUser.setUserName("Jhon");
        dbUser.setPassword("password");

        Mockito.doReturn(Optional.of(dbUser))
                    .when(userRepo)
                    .findById(1l);

        Assert.assertTrue(userService.editUser(user, 1l));
        Assert.assertEquals("Aleksandr", user.getUserName());
        Assert.assertEquals("1234456", user.getPassword());
        Assert.assertEquals(Long.valueOf(1l), user.getId());

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void editUserFailTest() throws UserInteractionException{
        UserEntity user = new UserEntity();
        user.setUserName("Aleksandr Izotov");

        Mockito.doReturn(Optional.ofNullable(null))
                .when(userRepo)
                .findById(1l);

        Assert.assertFalse(userService.editUser(user, 1l));
        Mockito.verify(userRepo, Mockito.times(0)).save(user);
    }

}