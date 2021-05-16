package ru.izotov.userphonebooks.exceptions.field;

public class FieldAlreadyExistException extends CheckingFieldsException {
    public FieldAlreadyExistException(String message) {
        super(message);
    }
}
