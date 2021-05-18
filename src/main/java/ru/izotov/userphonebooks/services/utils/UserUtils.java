package ru.izotov.userphonebooks.services.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.repositories.UserRepo;

@Service
public class UserUtils {

    @Autowired
    private UserRepo repo;

    public UserEntity findByIdOrThrows(Long id) throws UserInteractionException {
        return repo
                .findById(id)
                .orElseThrow(() -> new UserInteractionException(String.format("User with id %d not found", id)));
    }
}
