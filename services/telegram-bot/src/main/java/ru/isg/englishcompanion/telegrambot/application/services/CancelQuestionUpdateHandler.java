package ru.isg.englishcompanion.telegrambot.application.services;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.isg.englishcompanion.common.dto.enginecommands.CancelQuestionCommandDto;
import ru.isg.englishcompanion.telegrambot.infrastructure.messaging.EngineCommandsPublisher;

@Component
@Validated
@RequiredArgsConstructor
public class CancelQuestionUpdateHandler implements CommandUpdateHandler {

    private final EngineCommandsPublisher engineCommandsPublisher;

    @Override
    public boolean isCompatible(@NotNull Update update) {
        String[] lines = update.getMessage().getText().split("\\n");
        return lines.length == 1 && (lines[0].equals(".") || lines[0].equals("-"));
    }

    @Override
    public void handleUpdate(@NotNull Update update) {

        CancelQuestionCommandDto commandDto = new CancelQuestionCommandDto(
                update.getMessage().getChatId(),
                update.getMessage().getMessageId());
        engineCommandsPublisher.publish(commandDto);
    }
}
