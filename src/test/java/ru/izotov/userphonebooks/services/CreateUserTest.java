package ru.izotov.userphonebooks.services;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.models.User;
import ru.izotov.userphonebooks.repositories.UserRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
class CreateUserTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepo userRepo;

    @Test
    void createUser_Aleksandr_Izotov_returned_True() throws UserInteractionException {
        UserEntity user = new UserEntity();
        user.setUserName("Aleksandr Izotov");
        user.setPassword("Intern");

        Assert.assertTrue(userService.createUser(user));
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void createUser_Aleksandr_Izotov_returned_False() throws UserInteractionException {
        UserEntity user = new UserEntity();
        user.setUserName("Aleksandr Izotov");

        Mockito.doReturn(new UserEntity())
                .when(userRepo)
                .findByUserName("Aleksandr Izotov");

        Assert.assertFalse(userService.createUser(user));
        Mockito.verify(userRepo, Mockito.times(0)).save(user);
    }
}