package ru.isg.englishcompanion.telegrambot.application.bot;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.isg.englishcompanion.common.dto.enginecommands.SaveTranslationCommandDto;
import ru.isg.englishcompanion.telegrambot.application.messaging.EngineCommandsPublisher;

import java.util.Arrays;

@Component
@Validated
@RequiredArgsConstructor
public class SaveTranslationUpdateHandler implements CommandUpdateHandler {

    private final EngineCommandsPublisher engineCommandsPublisher;

    @Override
    public boolean isCompatible(@NotNull Update update) {
        String[] lines = update.getMessage().getText().split("\\n");
        return lines.length > 1;
    }

    @Override
    public void handleUpdate(@NotNull Update update) {

        String[] lines = update.getMessage().getText().split("\\n");

        SaveTranslationCommandDto commandDto = new SaveTranslationCommandDto(
                update.getMessage().getChatId(),
                update.getMessage().getMessageId(),
                lines[0],
                Arrays.asList(Arrays.copyOfRange(lines, 1, lines.length)));
        engineCommandsPublisher.publish(commandDto);
    }
}
