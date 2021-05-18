package ru.izotov.userphonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.models.User;
import ru.izotov.userphonebooks.repositories.UserRepo;

import java.util.List;
import java.util.Optional;
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
                .orElseThrow(()->new UserInteractionException(String.format("%s - required", "Username")))
                .isEmpty()
        ){throw new UserInteractionException("Username cannot be empty");}

        //Проверка пароля на соответствие
        Optional<String> password = Optional.ofNullable(user.getPassword());
        if(password
                .orElseThrow(()->new UserInteractionException(String.format("%s - required", "Password")))
                .length() < 4
        ){throw new UserInteractionException("Password must be more than 4 characters long");}

        userRepo.save(user);
        return true;
    }

    public User findById(Long id) throws UserInteractionException {
        return User.toModel(userRepo.findById(id)
                .orElseThrow(() -> new UserInteractionException(String.format("User with id %d not found", id))));
    }

    public boolean editUser(UserEntity userEntity, Long id) throws UserInteractionException{
        Optional<UserEntity> oU;
        if(!(oU = userRepo.findById(id))
                .isPresent()){
            return false;
        }
        UserEntity dbUser = oU.get();
        //Проверки на соответствие имени и пароля
        if(Optional.ofNullable(dbUser.getUserName())
                .orElseThrow(()->new UserInteractionException(String.format("%s - required", "Username")))
                .isEmpty()
        ){throw new UserInteractionException("Username cannot be empty");}

        if(Optional.ofNullable(dbUser.getPassword())
                .orElseThrow(()->new UserInteractionException(String.format("%s - required", "Password")))
                .length() < 4
        ){throw new UserInteractionException("Password must be more than 4 characters long");}

        userEntity.setBookEntryEntity(dbUser.getBookEntryEntity());
        userEntity.setBookEntities(dbUser.getBookEntities());
        userEntity.setId(dbUser.getId());
        userRepo.save(userEntity);
        return true;
    }

    public Long delete(Long id) throws UserInteractionException {
        userRepo.findById(id).orElseThrow(()->new UserInteractionException(String.format("User with id %d not found", id)));
        userRepo.deleteById(id);
        return id;
    }
}
