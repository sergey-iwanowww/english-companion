package ru.isg.englishcompanion.telegrambot.application.services;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@Validated
@RequiredArgsConstructor
public class CommandUpdateHandlerFactory {

    private final List<CommandUpdateHandler> updateHandlers;

    @NotNull
    public CommandUpdateHandler getInstance(@NotNull Update update) {

        return updateHandlers.stream()
                .filter(uh -> uh.isCompatible(update))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(
                        "Not found compatible handler for update: " + update.toString()));
    }
}
