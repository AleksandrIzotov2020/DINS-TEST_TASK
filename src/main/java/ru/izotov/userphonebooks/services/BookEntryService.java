package ru.izotov.userphonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.izotov.userphonebooks.entities.BookEntryEntity;
import ru.izotov.userphonebooks.entities.PhoneBookEntity;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.models.BookEntry;
import ru.izotov.userphonebooks.models.PhoneBook;
import ru.izotov.userphonebooks.repositories.PhoneBookRepo;
import ru.izotov.userphonebooks.repositories.BookEntryRepo;
import ru.izotov.userphonebooks.repositories.UserRepo;
import ru.izotov.userphonebooks.services.utils.UserUtils;

import java.util.*;

@Service
public class BookEntryService {

    @Autowired
    private PhoneBookRepo bookRepo;
    @Autowired
    private BookEntryRepo entryRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserUtils userUtils;


    public PhoneBook getAllEntriesForUser(Long user_id) throws Exception {
        UserEntity userEntity = userUtils.findByIdOrThrows(user_id);
        List<PhoneBookEntity> book = userEntity.getBookEntities();

        if(book.isEmpty()){
            throw new UserInteractionException(String.format("User %s has no entries in the phone book", userEntity.getUserName()));
        }

        return PhoneBook.toModel(book);
    }

    public Long delete(Long id, Long entry_id) throws UserInteractionException {
        UserEntity user = userUtils.findByIdOrThrows(id);

        if(user.getBookEntities().stream().noneMatch(b -> b.getId() == entry_id)){
            throw new UserInteractionException(String.format("Entry with id %d not found", entry_id));
        }
        bookRepo.deleteById(entry_id);
        return entry_id;
    }



    public PhoneBook findByPhoneNumber(Long user_id, String phoneNumber) throws UserInteractionException {
        UserEntity user = userUtils.findByIdOrThrows(user_id);
        List<PhoneBookEntity> book = user.getBookEntities();
        PhoneBookEntity entry = book.stream()
                .filter(b -> b.getEntry().getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElseThrow(()-> new UserInteractionException(String.format("No phone number %s was found for user %s in the phone book", phoneNumber, user.getUserName())));
        return PhoneBook.toModel(entry);
    }



    public BookEntry editEntry(BookEntryEntity editEntry, Long entry_id, Long user_id) throws UserInteractionException {
        UserEntity user = userUtils.findByIdOrThrows(user_id);

        BookEntryEntity entry = user.getBookEntities().stream()
                .filter(entry1 -> entry1.getId().equals(entry_id))
                .findFirst()
                .orElseThrow(()->new UserInteractionException(String.format("No entry with id %s found", entry_id)))
                .getEntry();

        Optional.ofNullable(editEntry.getUserName())
                .ifPresent(name -> {
            if(!name.isEmpty()
                    && name.length() < 50){
                entry.setUser(null);
                entry.setUserName(name);
            }
        });

        Optional<String> phoneNumber = Optional.ofNullable(editEntry.getPhoneNumber());
        if(phoneNumber.isPresent() && !(phoneNumber.get().length() > 6)){
            entry.setPhoneNumber(phoneNumber.get());
        }

        Optional.ofNullable(editEntry.getPhoneNumber())
                .ifPresent(number ->{
            if(!number.isEmpty()
                    && number.length() > 6
                    && number.length() < 30){
                entry.setPhoneNumber(number);
            }
        });
        entryRepo.save(entry);
        return BookEntry.toModel(entry);
    }

    public BookEntry createEntry(BookEntryEntity newEntry, Long user_id) throws UserInteractionException {
        UserEntity owner = userUtils.findByIdOrThrows(user_id);

        String userName = newEntry.getUserName();
        String phoneNumber = newEntry.getPhoneNumber();

        if(owner.getBookEntities().stream().anyMatch(b -> b.getEntry().getPhoneNumber() == phoneNumber)){
            throw new UserInteractionException("An entry with this number already exists");
        }

        if(userName==null || userName.isEmpty()
                || userName.length() > 50){
            throw new UserInteractionException("The new entry contains an invalid user name");
        }

        if(phoneNumber==null || phoneNumber.isEmpty()
                || phoneNumber.length() < 4 || phoneNumber.length() > 30){
            throw new UserInteractionException("The new entry contains an invalid phone number");
        }

        BookEntryEntity dbEntry = entryRepo.findByPhoneNumber(phoneNumber).orElse(newEntry);

        /* Если запись не найдена по номеру телефона -> ищем совпадение по имени
         * в таблице User и присваеваем его если нашли
         * * */
        if(dbEntry == newEntry){
            UserEntity user;
            if((user = userRepo.findByUserName(dbEntry.getUserName())) !=null ){
                dbEntry.setUser(user);
            }
            /* Сравниваем имена у двух записей:
             *       1. Имена совпали -> вернули запись из базы
             *       2. Имена разные -> бросили исключение
             * */
        }else if(!dbEntry.getUserName().equals(newEntry.getUserName())){
            throw new UserInteractionException("A user with this phone number already exists");
        }

        //newEntry = entryUtils.findByUsernameAndPhoneNumberOrThrows(newEntry);
        dbEntry = entryRepo.save(dbEntry);
        PhoneBookEntity newBookEntry = new PhoneBookEntity();
        newBookEntry.setOwner(owner);
        newBookEntry.setEntry(dbEntry);
        bookRepo.save(newBookEntry);
        return BookEntry.toModel(dbEntry);
    }
}
