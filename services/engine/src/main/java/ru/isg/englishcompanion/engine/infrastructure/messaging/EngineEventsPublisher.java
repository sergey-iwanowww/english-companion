package ru.isg.englishcompanion.engine.infrastructure.messaging;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.common.dto.engineevents.EventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAskedEventDto;
import ru.isg.englishcompanion.common.messaging.MessagePublisher;

@Component
@Validated
@RequiredArgsConstructor
public class EngineEventsPublisher {

    private final MessagePublisher messagePublisher;

    @Value("${engine.events.destination.name:engine-events}")
    private String engineEventsDestinationName;

    public void publish(@NotNull EventDto eventDto) {
        messagePublisher.publish(engineEventsDestinationName, eventDto);
    }

    public void publish(@NotNull QuestionAskedEventDto questionAskedEventDto) {
        messagePublisher.publish(engineEventsDestinationName, questionAskedEventDto);
    }
}
