package ru.isg.englishcompanion.engine.infrastructure.messaging;

import com.rabbitmq.client.Channel;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.common.dto.enginecommands.AcceptAnswerCommandDto;
import ru.isg.englishcompanion.common.dto.enginecommands.CancelQuestionCommandDto;
import ru.isg.englishcompanion.common.dto.enginecommands.SaveTranslationCommandDto;
import ru.isg.englishcompanion.engine.application.exceptions.QuestionNotFoundException;
import ru.isg.englishcompanion.engine.application.services.EngineCommandsHandler;

import java.io.IOException;

@Component
@Validated
@RequiredArgsConstructor
@RabbitListener(
        ackMode = "MANUAL",
        queuesToDeclare = @Queue(name = "${engine.commands.destination.name:engine-commands}", durable = "true"))
@Slf4j
public class EngineCommandsListener {

    private final EngineCommandsHandler engineCommandsHandler;

    /**
     * Обрабатывает сообщение с командой сохранения перевода.
     * В случае любого исключения обработка сообщения считается неудачной, получение сообщения не подтверждается.
     */
    @RabbitHandler
    @Transactional
    public void handleCommand(@NotNull SaveTranslationCommandDto command, @NotNull Channel channel,
            @NotNull @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        engineCommandsHandler.handleCommand(command);
        channel.basicAck(tag, false);
    }

    /**
     * Обрабатывает сообщение с командой сохранения перевода.
     * В случае исключения об отсутствии вопроса обработка сообщения считается успешной, получение сообщения подтверждается.
     * В случае любого другого исключения обработка сообщения считается неудачной, получение сообщения не подтверждается.
     */
    @RabbitHandler
    @Transactional
    public void handleCommand(@NotNull AcceptAnswerCommandDto command, @NotNull Channel channel,
            @NotNull @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            engineCommandsHandler.handleCommand(command);
            channel.basicAck(tag, false);
        } catch (QuestionNotFoundException e) {
            log.error(e.getMessage(), e);
            channel.basicAck(tag, false);
        }
    }

    /**
     * Обрабатывает сообщение с командой отмены вопроса.
     * В случае исключения об отсутствии вопроса обработка сообщения считается успешной, получение сообщения подтверждается.
     * В случае любого другого исключения обработка сообщения считается неудачной, получение сообщения не подтверждается.
     */
    @RabbitHandler
    @Transactional
    public void handleCommand(@NotNull CancelQuestionCommandDto command, @NotNull Channel channel,
            @NotNull @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            engineCommandsHandler.handleCommand(command);
            channel.basicAck(tag, false);
        } catch (QuestionNotFoundException e) {
            log.error(e.getMessage(), e);
            channel.basicAck(tag, false);
        }
    }
}
