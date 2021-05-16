package ru.izotov.userphonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.izotov.userphonebooks.entities.PhoneBookEntity;
import ru.izotov.userphonebooks.entities.PhoneNumberEntity;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.field.*;
import ru.izotov.userphonebooks.exceptions.phonebook.NoEntriesFoundException;
import ru.izotov.userphonebooks.exceptions.phonebook.PhoneBookException;
import ru.izotov.userphonebooks.models.PhoneBook;
import ru.izotov.userphonebooks.models.PhoneNumber;
import ru.izotov.userphonebooks.repositories.PhoneBookRepo;
import ru.izotov.userphonebooks.repositories.PhoneNumberRepo;
import ru.izotov.userphonebooks.repositories.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PhoneNumberService {

    @Autowired
    private PhoneBookRepo bookRepo;
    @Autowired
    private PhoneNumberRepo numberRepo;
    @Autowired
    private UserRepo userRepo;

    public PhoneNumber editEntry(PhoneNumberEntity numberEntity, Long entry_id, Long user_id) throws CheckingFieldsException, PhoneBookException {
        UserEntity user = findUser(user_id);
        PhoneBookEntity oldEntry = user.getBookEntities().stream()
                .filter(entry -> entry.getId().equals(entry_id))
                .findFirst()
                .orElseThrow(()->new NoEntriesFoundException(String.format("No entry with id %s found", entry_id)));

        //Проверка имени пользователя на соответствие
        String username = checkingUsername(numberEntity.getUsername());

        //Проверка телефонного номера на соответствие
        String phonenumber = checkingPhonenumber(numberEntity.getPhonenumber());
        /*
        *  1) Присутствие новой записи в базе (PhoneNumber)
        *       1.1) Да -> oldEntry.setNumber(...) and save
        *       1.2) Нет -> Добавить запись в PhoneNumber, присвоить записи пользователя (Если есть) -> (1.1)
        * */
        return null;
    }

    //По возможности переделать
    public PhoneNumber createRecord(PhoneNumberEntity newEntry, Long user_id) throws CheckingFieldsException {
        //Проверка имени пользователя на соответствие
        String username = checkingUsername(newEntry.getUsername());

        //Проверка телефонного номера на соответствие
        String phonenumber = checkingPhonenumber(newEntry.getPhonenumber());

        Optional<PhoneNumberEntity> entry;
        if((entry = numberRepo.findByPhonenumber(phonenumber)).isPresent()){
            if(!entry.get().getUsername().equals(username)){
                //У двух разный username не может быть одинаковых телефонных номеров
                throw new FieldAlreadyExistException("A user with this phone number already exists");
            }
            newEntry = entry.get();
        }

        //Проверка владельца телефонной книги на присутствие в базе
        UserEntity owner = findUser(user_id);


        //Ищет пользователя с таким именем и присваивает ему номер в случае его нахождения в базе
        Optional<UserEntity> user;
        if(newEntry.getUser() == null && (user= userRepo.findByUsername(username)).isPresent()){
            newEntry.setUser(user.get());
        }

        if(bookRepo
                .findByUser_id(owner.getId())
                .stream()
                .noneMatch(rec -> rec.getNumber().getPhonenumber().equals(phonenumber))){

            numberRepo.save(newEntry);
            //Создается запись и сохраняется в телефонную книгу
            PhoneBookEntity bookEntity = new PhoneBookEntity();
            bookEntity.setUser(owner);
            bookEntity.setNumber(newEntry);
            bookRepo.save(bookEntity);
        }else {
            throw new FieldAlreadyExistException("This entry already exists in the phone book");
        }

        return PhoneNumber.toModel(newEntry);
    }

    private String checkingUsername(String username) throws CheckingFieldsException {
        if(username == null){
            throw new NotNullException(String.format("%s - required", "Username"));
        }
        if(username.isEmpty()){
            throw new FieldCannotBeEmptyException("Username cannot be empty");
        }
        return username;
    }

    private String checkingPhonenumber(String phonenumber) throws CheckingFieldsException {
        if(phonenumber == null){
            throw new NotNullException(String.format("%s - required", "Phonenumber"));
        }

        if(phonenumber.length() < 6){
            throw new FieldCannotBeEmptyException("Phonenumber must be more than 6 characters long");
        }
        return  phonenumber;
    }

    private UserEntity findUser(Long id) throws UserNotFoundException {
        return userRepo
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)));
    }


}
