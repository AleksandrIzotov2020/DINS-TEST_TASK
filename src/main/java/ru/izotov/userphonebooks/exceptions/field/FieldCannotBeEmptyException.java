package ru.izotov.userphonebooks.exceptions.field;

public class FieldCannotBeEmptyException extends CheckingFieldsException{
    public FieldCannotBeEmptyException(String message) {
        super(message);
    }
}
