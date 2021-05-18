package ru.izotov.userphonebooks.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.izotov.userphonebooks.entities.BookEntryEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.services.PhoneBookService;

@RestController
@RequestMapping("/books")
public class PhoneBookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneBookController.class);

    @Autowired
    private PhoneBookService bookService;

    @GetMapping
    public  ResponseEntity findByPhoneNumber(@RequestParam String phoneNumber){
        try{
            return ResponseEntity.ok(bookService.findByPhoneNumber(phoneNumber));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(bookService.findById(id));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity editEntry(@RequestBody BookEntryEntity entry,
                                    @PathVariable Long id){
        try{
            if(bookService.editEntry(entry,id)){
                return ResponseEntity.ok("The entry was changed");
            } return ResponseEntity.badRequest().body(String.format("Entry with id %s not found", id));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }



    @DeleteMapping
    public ResponseEntity deleteEntry(@RequestParam Long id){
        try{
            return ResponseEntity.ok(String.format("An entry with id %s has been deleted", bookService.delete(id)));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

    private ResponseEntity getUnexpectedException(Throwable throwable){
        LOGGER.error("An error occurred", throwable);
        return ResponseEntity.badRequest().body(String.format("An error occurred.\n%s", throwable.getMessage())); // Почитать про этот класс
    }
}
