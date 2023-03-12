package ru.isg.englishcompanion.telegrambot.application.bot;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@Validated
public class LoggingUpdateHandler implements UpdateHandler {
    @Override
    public void handleUpdate(@NotNull Update update) {
        log.info("update received: " + update.toString());
    }
}
