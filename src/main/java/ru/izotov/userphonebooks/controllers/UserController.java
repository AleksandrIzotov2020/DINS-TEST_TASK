package ru.izotov.userphonebooks.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.field.CheckingFieldsException;
import ru.izotov.userphonebooks.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity getOneUser(@PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.findById(id));
        }catch (CheckingFieldsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage())); // Почитать про этот класс
        }
    }

   @GetMapping
    public ResponseEntity getUserByName(@RequestParam String username){
       try {
           return ResponseEntity.ok(userService.containsUsername(username));
       }catch (Exception e){
           return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage())); // Почитать про этот класс
       }
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        try {
            return ResponseEntity.ok(userService.findAll());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage())); // Почитать про этот класс
        }
    }

    @PostMapping
    public ResponseEntity registration(@RequestBody UserEntity user){
        try {
            userService.registration(user);
            return ResponseEntity.ok(String.format("%s saved successfully", user.getUsername()));
        }catch (CheckingFieldsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage())); // Почитать про этот класс
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity editUser(@RequestBody UserEntity userEntity,
                                   @PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.editUser(userEntity, id));
        }catch (CheckingFieldsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage())); // Почитать про этот класс
        }
    }

    @DeleteMapping
    public ResponseEntity deleteUser(@RequestParam Long id){
        try {
            return ResponseEntity.ok(String.format("A user with id %s has been deleted", userService.delete(id)));
        }catch (CheckingFieldsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(String.format("An error occurred. %s", e.getMessage())); // Почитать про этот класс
        }
    }
}
