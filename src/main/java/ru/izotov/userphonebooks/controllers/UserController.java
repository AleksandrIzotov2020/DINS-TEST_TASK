package ru.izotov.userphonebooks.controllers;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity getOneUser(@PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.findById(id));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

   @GetMapping
    public ResponseEntity getUserByName(@RequestParam String username){
       try {
           return ResponseEntity.ok(userService.containsUserName(username));
       }catch (Exception e){
           return getUnexpectedException(e);
       }
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        try {
            return ResponseEntity.ok(userService.findAll());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserEntity user){
        try {
            if(userService.createUser(user)){
                return ResponseEntity.ok(String.format("%s successfully saved with id %s", user.getUserName(), user.getId()));
            } return ResponseEntity.badRequest().body("This user already exists");
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity editUser(@RequestBody UserEntity user,
                                   @PathVariable Long id){
        try {
            if(userService.editUser(user, id)){
                return ResponseEntity.ok(String.format("The user %s was changed", user.getUserName()));
            } return ResponseEntity.badRequest().body("Something went wrong!");
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
           return getUnexpectedException(e);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteUser(@RequestParam Long id){
        try {
            return ResponseEntity.ok(String.format("A user with id %s has been deleted", userService.delete(id)));
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
