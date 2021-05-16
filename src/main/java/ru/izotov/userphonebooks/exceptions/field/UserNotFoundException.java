package ru.izotov.userphonebooks.exceptions.field;

public class UserNotFoundException extends CheckingFieldsException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
