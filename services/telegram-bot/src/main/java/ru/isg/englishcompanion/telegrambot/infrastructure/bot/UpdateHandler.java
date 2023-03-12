package ru.isg.englishcompanion.telegrambot.infrastructure.bot;

import jakarta.validation.constraints.NotNull;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    void handleUpdate(@NotNull Update update);
}
