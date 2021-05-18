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
import ru.izotov.userphonebooks.services.utils.BookEntryUtils;
import ru.izotov.userphonebooks.services.utils.UserUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private BookEntryUtils entryUtils;


    public PhoneBook getAllEntriesForUser(Long user_id) throws Exception {
        UserEntity userEntity = userRepo
                .findById(user_id)
                .orElseThrow(() -> new UserInteractionException(String.format("User with id %d not found", user_id)));

        List<PhoneBookEntity> entries =  bookRepo.findByOwner_id(user_id);

        if(entries.isEmpty()){
            throw new UserInteractionException(String.format("User %s has no entries in the phone book", userEntity.getUserName()));
        }

        return PhoneBook.toModel(entries);
    }

    public Long delete(Long id, Long entry_id) throws UserInteractionException {
        UserEntity user = userUtils.findByIdOrThrows(id);

        PhoneBookEntity entry = user.getBookEntities().stream()
                .filter(b -> b.getId().equals(entry_id))
                .findFirst()
                .orElseThrow(()->new UserInteractionException(String.format("Entry with id %d not found", entry_id)));
        bookRepo.findById(entry_id);
        bookRepo.deleteById(entry_id);
        return entry_id;
    }


    public PhoneBook findBiId(Long user_id, Long entry_id) throws UserInteractionException {
        UserEntity user = userUtils.findByIdOrThrows(user_id);
        List<PhoneBookEntity> book = user.getBookEntities();
        PhoneBookEntity entry = book.stream()
                .filter(b->b.getId().equals(entry_id))
                .findFirst()
                .orElseThrow(()->new UserInteractionException(String.format("User %s does not have an entry with id %s", user.getUserName(), entry_id)));
        return PhoneBook.toModel(entry);

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

        PhoneBookEntity bookEntry = user.getBookEntities().stream()
                .filter(entry -> entry.getId().equals(entry_id))
                .findFirst()
                .orElseThrow(()->new UserInteractionException(String.format("No entry with id %s found", entry_id)));

        Optional<String> userName = Optional.ofNullable(editEntry.getUserName());
        if(userName.isPresent() && !userName.get().isEmpty()){
            bookEntry.getEntry().setUserName(userName.get());
            UserEntity userPhone = bookEntry.getEntry().getUser();
            if(userPhone!=null) userPhone.setUserName(userName.get());
        }

        Optional<String> phoneNumber = Optional.ofNullable(editEntry.getPhoneNumber());
        if(phoneNumber.isPresent() && !(phoneNumber.get().length() > 6)){
            bookEntry.getEntry().setPhoneNumber(phoneNumber.get());
        }
        bookRepo.save(bookEntry);
        return BookEntry.toModel(bookEntry.getEntry());
    }

    //По возможности переделать
    public PhoneBook createEntry(BookEntryEntity newEntry, Long user_id) throws UserInteractionException {
        UserEntity owner = userUtils.findByIdOrThrows(user_id);
        if(!entryUtils.isUserName(newEntry.getUserName()) || !entryUtils.isPhoneNumber(newEntry.getPhoneNumber())){
            throw new UserInteractionException("The new entry contains an invalid username and / or password");
        }
        newEntry = entryUtils.findByUsernameAndPhoneNumberOrThrows(newEntry);

        String phoneNumber = newEntry.getPhoneNumber();
        List<PhoneBookEntity> book = owner.getBookEntities();
        if(book.stream().anyMatch(b -> b.getEntry().getPhoneNumber() == phoneNumber)){
            throw new UserInteractionException("An entry with this number already exists");
        }
        //numberRepo.save(newEntry);
        PhoneBookEntity newBookEntry = new PhoneBookEntity();
        newBookEntry.setOwner(owner);
        newBookEntry.setEntry(newEntry);
        bookRepo.save(newBookEntry);
        return PhoneBook.toModel(newBookEntry);


        /*
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

        return PhoneNumber.toModel(newEntry);*/
    }
}
