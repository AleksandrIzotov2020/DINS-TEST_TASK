package ru.izotov.userphonebooks.exceptions.field;

public class NotNullException extends CheckingFieldsException{
    public NotNullException(String message) {
        super(message);
    }
}
