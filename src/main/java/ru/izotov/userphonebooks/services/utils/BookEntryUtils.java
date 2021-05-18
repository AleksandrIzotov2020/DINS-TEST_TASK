package ru.izotov.userphonebooks.services.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.izotov.userphonebooks.entities.BookEntryEntity;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.repositories.BookEntryRepo;
import ru.izotov.userphonebooks.repositories.UserRepo;

import java.util.Optional;


@Service
public class BookEntryUtils {

    @Autowired
    private BookEntryRepo repo;
    @Autowired
    private UserRepo userRepo;

    public boolean isUserName(String userName){
        if(userName == null){
            return false;
            //throw new NotNullException(String.format("%s - required", "UserName"));
        }
        if(userName.isEmpty()){
            return false;
            //throw new FieldCannotBeEmptyException("UserName cannot be empty");
        }

        return true;
    }

    public boolean isPhoneNumber(String phoneNumber){
        if(phoneNumber == null){
            return false;
            //throw new NotNullException(String.format("%s - required", "PhoneNumber"));
        }

        if(phoneNumber.length() < 6){
            return false;
            //throw new FieldCannotBeEmptyException("PhoneNumber must be more than 6 characters long");
        }
        return true;
    }

    public BookEntryEntity findByUsernameAndPhoneNumberOrThrows(BookEntryEntity entity) throws UserInteractionException {
        BookEntryEntity dbEntry = repo.findByPhoneNumber(entity.getPhoneNumber()).orElse(entity);
        /* Если запись не найдена по номеру телефона -> ищем совпадение по имени
         * в таблице User и присваеваем его если нашли
         * * */
        if(dbEntry == entity){
            UserEntity user;
            if((user = userRepo.findByUserName(dbEntry.getUserName())) !=null ){
                dbEntry.setUser(user);
            }
            /* Сравниваем имена у двух записей:
            *       1. Имена совпали -> вернули запись из базы
            *       2. Имена разные -> бросили исключение
            * */
        }else if(dbEntry.getUserName().equals(entity.getUserName())){
            return dbEntry;
        }else if(!dbEntry.getUserName().equals(entity.getUserName())){
            throw new UserInteractionException("A user with this phone number already exists");
        }
        return repo.save(dbEntry);
    }



}
