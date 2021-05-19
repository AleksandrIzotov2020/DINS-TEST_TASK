package ru.izotov.userphonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.izotov.userphonebooks.entities.BookEntryEntity;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.models.User;
import ru.izotov.userphonebooks.repositories.BookEntryRepo;
import ru.izotov.userphonebooks.repositories.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BookEntryRepo entryRepo;

    public List<User> findAll(){
        return StreamSupport.stream(userRepo.findAll().spliterator(),false)
                .map(User::toModel)
                .collect(Collectors.toList());
    }

    public List<User> containsUserName(String username){
        return userRepo
                .findByUserNameContaining(username)
                .stream()
                .map(User::toModel)
                .collect(Collectors.toList());
    }

    public boolean createUser(UserEntity user) throws UserInteractionException {

        if(userRepo.findByUserName(user.getUserName()) != null){
            return  false;
        }

        // Проверка имени пользователя на соответствие
        Optional<String> userName = Optional.ofNullable(user.getUserName());
        if(userName
                .orElseThrow(()->new UserInteractionException(String.format("%s - required", "User name")))
                .isEmpty()
                || userName.get().length() > 50
        ){throw new UserInteractionException("User name cannot be empty or longer than 50 characters");}

        //Проверка пароля на соответствие
        Optional<String> password = Optional.ofNullable(user.getPassword());
        if(password
                .orElseThrow(()->new UserInteractionException(String.format("%s - required", "Password")))
                .length() < 4
                || password.get().length() > 30
        ){throw new UserInteractionException("The password must contain between 4 and 30 characters");}

        UserEntity dbUser = userRepo.save(user);

        //Проверка на наличие номера телефона с таким же user name
        entryRepo.findByUserName(userName.get()).ifPresent(e -> {
            e.setUser(dbUser);
            entryRepo.save(e);});

        return true;
    }

    public User findById(Long id) throws UserInteractionException {
        return User.toModel(userRepo.findById(id)
                .orElseThrow(() -> new UserInteractionException(String.format("User with id %d not found", id))));
    }

    public boolean editUser(UserEntity userEntity, Long id) throws UserInteractionException{
        AtomicBoolean result = new AtomicBoolean(false);
        userRepo.findById(id).ifPresent(dbUser -> {
            boolean isUserName = true;
            boolean isPassword = true;
            // Проверка имени пользователя на соответствие
            Optional<String> userName = Optional.ofNullable(userEntity.getUserName());
            if(!userName.isPresent() || userName.get().length() > 50){
                isUserName = false;
                userEntity.setUserName(dbUser.getUserName());
            }

            //Проверка пароля на соответствие
            Optional<String> password = Optional.ofNullable(userEntity.getPassword());
            int length;
            if(!password.isPresent() ||   (length = password.get().length()) < 4 || length > 30){
                isPassword = false;
                userEntity.setPassword(dbUser.getPassword());
            }

            BookEntryEntity entry = dbUser.getBookEntryEntity();
            if(entry != null && isUserName){
                entry.setUserName(userEntity.getUserName());
                userEntity.setBookEntryEntity(entry);
            }

            userEntity.setBookEntities(dbUser.getBookEntities());
            userEntity.setId(dbUser.getId());
            userRepo.save(userEntity);
            if(isUserName || isPassword){
                result.set(true);
            }
        });
        return result.get();
    }

    public Long delete(Long id) throws UserInteractionException {
        userRepo.findById(id).orElseThrow(()->new UserInteractionException(String.format("User with id %d not found", id)));
        userRepo.deleteById(id);
        return id;
    }
}
