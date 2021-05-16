package ru.izotov.userphonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.field.*;
import ru.izotov.userphonebooks.models.User;
import ru.izotov.userphonebooks.repositories.UserRepo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// Проверки на максимальную длину поля
@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public Iterable<User> findAll(){
        return StreamSupport.stream(userRepo.findAll().spliterator(),false)
                .map(User::toModel)
                .collect(Collectors.toList());
    }

    public List<User> containsUsername(String username){
        return userRepo
                .findByUsernameContaining(username)
                .stream()
                .map(User::toModel)
                .collect(Collectors.toList());
    }

    public User registration(UserEntity user) throws CheckingFieldsException {
        // Проверка имени пользователя на соответствие
        checkingUsername(user.getUsername());

        //Проверка пароля на соответствие
        checkingPassword(user.getPassword());

        return User.toModel(userRepo.save(user));
    }

    public User findById(Long id) throws CheckingFieldsException {
        return User.toModel(userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id))));
    }

    public User editUser(UserEntity userEntity, Long id) throws CheckingFieldsException{
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)));

        //Проверки на соответствие имени и пароля
        checkingUsername(userEntity.getUsername());
        checkingPassword(userEntity.getPassword());

        user.setUsername(userEntity.getUsername());
        user.setPassword(userEntity.getPassword());
        user.getPhoneNumberEntities().setUsername(userEntity.getUsername());
        return User.toModel(userRepo.save(user));
    }

    public Long delete(Long id) throws CheckingFieldsException {
        if(userRepo.findById(id).isPresent()){
            userRepo.deleteById(id);
        }else{
            throw new UserNotFoundException(String.format("User with id %d not found", id));
        }
        return id;
    }

    private void checkingUsername(String username) throws CheckingFieldsException {
        if(username == null){
            throw new NotNullException(String.format("%s - required", "Username"));
        }

        if(username.isEmpty()){
            throw new FieldCannotBeEmptyException("Username cannot be empty");
        }

        if(userRepo.findByUsername(username).isPresent()){
            throw new FieldAlreadyExistException(String.format("A user named %s already exists", username));
        }
    }

    private void checkingPassword(String password) throws CheckingFieldsException {
        if(password == null){
            throw new NotNullException(String.format("%s - required", "Password"));
        }

        if(password.length() < 4){
            throw new FieldCannotBeEmptyException("Password must be more than 4 characters long");
        }
    }
}
