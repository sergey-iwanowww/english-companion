package ru.isg.englishcompanion.telegrambot.application.bot;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.isg.englishcompanion.common.dto.enginecommands.AcceptAnswerCommandDto;
import ru.isg.englishcompanion.telegrambot.application.messaging.EngineCommandsPublisher;

@Component
@Validated
@RequiredArgsConstructor
public class AcceptAnswerUpdateHandler implements CommandUpdateHandler {

    private final EngineCommandsPublisher engineCommandsPublisher;

    @Override
    public boolean isCompatible(@NotNull Update update) {
        String[] lines = update.getMessage().getText().split("\\n");
        return lines.length == 1 && !lines[0].equals(".") && !lines[0].equals("-");
    }

    @Override
    public void handleUpdate(@NotNull Update update) {

        String[] lines = update.getMessage().getText().split("\\n");

        AcceptAnswerCommandDto commandDto = new AcceptAnswerCommandDto(
                update.getMessage().getChatId(),
                update.getMessage().getMessageId(),
                lines[0]);
        engineCommandsPublisher.publish(commandDto);
    }
}
