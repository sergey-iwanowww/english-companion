package ru.isg.englishcompanion.telegrambot.infrastructure.messaging;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.common.dto.enginecommands.CommandDto;
import ru.isg.englishcompanion.common.messaging.MessagePublisher;

@Component
@Validated
@RequiredArgsConstructor
public class EngineCommandsPublisher {

    private final MessagePublisher messagePublisher;

    @Value("${engine.commands.destination.name:engine-commands}")
    private String engineCommandsDestinationName;

    public void publish(@NotNull CommandDto commandDto) {
        messagePublisher.publish(engineCommandsDestinationName, commandDto);
    }
}
