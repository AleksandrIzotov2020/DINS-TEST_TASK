package ru.izotov.userphonebooks.exceptions.phonebook;

public class NoEntriesFoundException extends PhoneBookException{
    public NoEntriesFoundException(String message) {
        super(message);
    }
}
