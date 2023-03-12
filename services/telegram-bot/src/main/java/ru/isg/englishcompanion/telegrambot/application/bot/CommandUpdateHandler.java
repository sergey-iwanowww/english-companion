package ru.isg.englishcompanion.telegrambot.application.bot;

import jakarta.validation.constraints.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandUpdateHandler {

    boolean isCompatible(@NotNull Update update);

    void handleUpdate(@NotNull Update update);
}