package ru.izotov.userphonebooks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.izotov.userphonebooks.entities.PhoneNumberEntity;
import ru.izotov.userphonebooks.exceptions.field.CheckingFieldsException;
import ru.izotov.userphonebooks.services.PhoneNumberService;

@RestController
@RequestMapping("/users/{id}/book")
public class PhoneNumberController {

    @Autowired
    private PhoneNumberService numberService;

    @PostMapping
    public ResponseEntity createEntry(@RequestBody PhoneNumberEntity numberEntity,
                                      @PathVariable Long id){
        try{
            return ResponseEntity.ok(numberService.createRecord(numberEntity, id));
        }catch (CheckingFieldsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage()));
        }
    }

    @PutMapping("/{entry_id}")
    public ResponseEntity editEntry (@RequestBody PhoneNumberEntity numberEntity,
                                     @PathVariable Long entry_id,
                                     @PathVariable Long id){
        try{
            return ResponseEntity.ok(numberService.editEntry(numberEntity, entry_id, id));
        }catch (CheckingFieldsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage()));
        }
    }

}
