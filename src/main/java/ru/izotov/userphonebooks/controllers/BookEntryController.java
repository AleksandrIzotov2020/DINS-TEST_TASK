package ru.izotov.userphonebooks.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.izotov.userphonebooks.entities.BookEntryEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.services.BookEntryService;

@RestController
@RequestMapping("/users/{id}/book")
public class BookEntryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookEntryController.class);

    @Autowired
    private BookEntryService entryService;

    @GetMapping("/all")
    public ResponseEntity getAllEntriesForUser(@PathVariable Long id){
        try{
            return ResponseEntity.ok(entryService.getAllEntriesForUser(id));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

    @GetMapping("/{entry_id}")
    public ResponseEntity findByPhoneNumber(@PathVariable Long id,
                                            @PathVariable Long entry_id){
        try{
            return ResponseEntity.ok(entryService.findById(id,entry_id));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

    @GetMapping
    public ResponseEntity findByPhoneNumber(@PathVariable Long id,
                                            @RequestParam String phoneNumber){
        try{
            return ResponseEntity.ok(entryService.findByPhoneNumber(id,phoneNumber));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }



    @PostMapping
    public ResponseEntity createEntry(@RequestBody BookEntryEntity entryEntity,
                                      @PathVariable Long id){
        try{
            return ResponseEntity.ok(entryService.createEntry(entryEntity, id));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }


    @PutMapping("/{entry_id}")
    public ResponseEntity editEntry (@RequestBody BookEntryEntity entryEntity,
                                     @PathVariable Long entry_id,
                                     @PathVariable Long id){
        try{
            return ResponseEntity.ok(entryService.editEntry(entryEntity, entry_id, id));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteEntry(@PathVariable Long id,
                                      @RequestParam Long entry_id){
        try{
            return ResponseEntity.ok(String.format("An entry with id %s has been deleted", entryService.delete(id, entry_id)));
        }catch (UserInteractionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return getUnexpectedException(e);
        }
    }

    private ResponseEntity getUnexpectedException(Throwable throwable){
        LOGGER.error("An error occurred", throwable);
        return ResponseEntity.badRequest().body(String.format("An error occurred.\n%s", throwable.getMessage()));
    }
}
