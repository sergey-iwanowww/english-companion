package ru.isg.englishcompanion.engine.application.exceptions;

public class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException(String message) {
        super(message);
    }
}
