package ru.isg.englishcompanion.common.messaging;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.common.dto.Message;

@Component
@Validated
@RequiredArgsConstructor
public class RabbitmqMessagePublisher implements MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(@NotEmpty String destinationName, @NotNull Message message) {
        rabbitTemplate.convertAndSend(destinationName, message);
    }
}
