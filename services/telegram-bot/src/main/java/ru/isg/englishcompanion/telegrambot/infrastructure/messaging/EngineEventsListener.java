package ru.isg.englishcompanion.telegrambot.infrastructure.messaging;

import com.rabbitmq.client.Channel;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredNotCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAskedEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionCancelledEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.TranslationSavedEventDto;
import ru.isg.englishcompanion.telegrambot.application.services.EngineEventsHandler;

import java.io.IOException;

@Component
@Validated
@RequiredArgsConstructor
@RabbitListener(
        ackMode = "MANUAL",
        queuesToDeclare = @Queue(name = "${engine.events.destination.name:engine-events}", durable = "true"))
public class EngineEventsListener {

    private final EngineEventsHandler engineEventsHandler;

    /**
     * Обрабатывает сообщение с событием сохранения перевода.
     * В случае любого исключения обработка сообщения считается неудачной, получение сообщения не подтверждается.
     */
    @RabbitHandler
    public void handleEvent(@NotNull TranslationSavedEventDto event, @NotNull Channel channel,
            @NotNull @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        engineEventsHandler.handleEvent(event);
        channel.basicAck(tag, false);
    }

    /**
     * Обрабатывает сообщение с событием задания вопроса.
     * В случае любого исключения обработка сообщения считается неудачной, получение сообщения не подтверждается.
     */
    @RabbitHandler
    public void handleEvent(@NotNull QuestionAskedEventDto event, @NotNull Channel channel,
            @NotNull @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        engineEventsHandler.handleEvent(event);
        channel.basicAck(tag, false);
    }

    /**
     * Обрабатывает сообщение с событием правильного ответа на вопрос.
     * В случае любого исключения обработка сообщения считается неудачной, получение сообщения не подтверждается.
     */
    @RabbitHandler
    public void handleEvent(@NotNull QuestionAnsweredCorrectlyEventDto event, @NotNull Channel channel,
            @NotNull @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        engineEventsHandler.handleEvent(event);
        channel.basicAck(tag, false);
    }

    /**
     * Обрабатывает сообщение с событием неправильного ответа на вопрос.
     * В случае любого исключения обработка сообщения считается неудачной, получение сообщения не подтверждается.
     */
    @RabbitHandler
    public void handleEvent(@NotNull QuestionAnsweredNotCorrectlyEventDto event, @NotNull Channel channel,
            @NotNull @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        engineEventsHandler.handleEvent(event);
        channel.basicAck(tag, false);
    }

    /**
     * Обрабатывает сообщение с событием отмены вопроса.
     * В случае любого исключения обработка сообщения считается неудачной, получение сообщения не подтверждается.
     */
    @RabbitHandler
    public void handleEvent(@NotNull QuestionCancelledEventDto event, @NotNull Channel channel,
            @NotNull @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        engineEventsHandler.handleEvent(event);
        channel.basicAck(tag, false);
    }
}
