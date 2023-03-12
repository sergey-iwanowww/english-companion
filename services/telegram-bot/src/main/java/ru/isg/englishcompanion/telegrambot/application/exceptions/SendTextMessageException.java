package ru.isg.englishcompanion.telegrambot.application.exceptions;

public class SendTextMessageException extends RuntimeException {
    public SendTextMessageException(Throwable cause) {
        super(cause);
    }
}
