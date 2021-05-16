package ru.izotov.userphonebooks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.izotov.userphonebooks.exceptions.field.CheckingFieldsException;
import ru.izotov.userphonebooks.exceptions.phonebook.PhoneBookException;
import ru.izotov.userphonebooks.services.PhoneBookService;

@RestController
@RequestMapping
public class PhoneBookController {

    @Autowired
    private PhoneBookService bookService;

    @GetMapping("/books/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(bookService.findById(id));
        }catch (PhoneBookException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage()));
        }
    }

    @GetMapping("/users/{id}/book")
    public ResponseEntity getAllRecordsForUser(@PathVariable Long id){
        try{
            return ResponseEntity.ok(bookService.getAllEntriesForUser(id));
        }catch (CheckingFieldsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (PhoneBookException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage()));
        }
    }







    /*@PutMapping
    public ResponseEntity editRecord(@RequestParam Long record_id){
        try{
            return null;
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. ", e.getMessage()));
        }
    }*/
}
