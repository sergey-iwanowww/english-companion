package ru.isg.englishcompanion.telegrambot.application.services;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.isg.englishcompanion.telegrambot.infrastructure.bot.UpdateHandler;

@Slf4j
@Component
@Validated
@RequiredArgsConstructor
@Primary
public class MessagingUpdateHandler implements UpdateHandler {

    private final CommandUpdateHandlerFactory commandUpdateHandlerFactory;

    @Override
    public void handleUpdate(@NotNull Update update) {
        log.info("update received: " + update.toString());
        commandUpdateHandlerFactory.getInstance(update)
                .handleUpdate(update);
    }
}
