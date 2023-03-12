package ru.isg.englishcompanion.telegrambot.application.messaging;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredNotCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAskedEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionCancelledEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.TranslationSavedEventDto;
import ru.isg.englishcompanion.telegrambot.application.bot.EnglishCompanionBot;

import static java.lang.String.format;

@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class EngineEventsHandler {

    private final EnglishCompanionBot bot;

    /**
     * Обрабатывает событие сохранения перевода.
     */
    public void handleEvent(@NotNull TranslationSavedEventDto event) {
        if (event.isUpdated()) {
            bot.sendTranslationUpdatedText(event.getChatId(), event.getSaveTranslationMessageId());
        } else {
            bot.sendTranslationCreatedText(event.getChatId(), event.getSaveTranslationMessageId());
        }
    }

    /**
     * Обрабатывает событие задания вопроса.
     */
    public void handleEvent(@NotNull QuestionAskedEventDto event) {
        bot.sendQuestionAddedText(event.getChatId(), event.getPhrase());
    }

    /**
     * Обрабатывает событие правильного ответа на вопрос.
     */
    public void handleEvent(@NotNull QuestionAnsweredCorrectlyEventDto event) {
        bot.sendQuestionAnsweredCorrectlyText(event.getChatId(), event.getPhrase(), event.getAnswerMessageId());
    }

    /**
     * Обрабатывает событие неправильного ответа на вопрос.
     */
    public void handleEvent(@NotNull QuestionAnsweredNotCorrectlyEventDto event) {
        bot.sendQuestionAnsweredNotCorrectlyText(event.getChatId(), event.getPhrase(), event.getAnswerMessageId());
    }

    /**
     * Обрабатывает событие отмены вопроса.
     */
    public void handleEvent(@NotNull QuestionCancelledEventDto event) {
        bot.sendQuestionCancelledText(event.getChatId(), event.getPhrase(), event.getCancelMessageId());
    }
}
