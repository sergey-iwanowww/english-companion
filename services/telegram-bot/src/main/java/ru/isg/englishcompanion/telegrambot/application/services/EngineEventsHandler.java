package ru.isg.englishcompanion.telegrambot.application.services;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredNotCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAskedEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionCancelledEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.TranslationSavedEventDto;
import ru.isg.englishcompanion.telegrambot.infrastructure.bot.EnglishCompanionBot;

import static java.lang.String.format;

@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class EngineEventsHandler {

    private final EnglishCompanionBot bot;

    public static final String TRANSLATION_CREATED_TEXT = "Перевод добавлен";
    public static final String TRANSLATION_UPDATED_TEXT = "Перевод обновлен";
    public static final String QUESTION_ADDED_TEXT = "\u2754 Переведите слово/фразу: \"%s\"";
    public static final String QUESTION_ANSWERED_CORRECTLY_TEXT = "\u2705 Правильный перевод слова/фразы: \"%s\"";
    public static final String QUESTION_ANSWERED_NOT_CORRECTLY_TEXT = "\u274C Неправильный перевод слова/фразы: \"%s\"";
    public static final String QUESTION_CANCELLED_TEXT = "\u2796 Отменен перевод слова/фразы: \"%s\"";

    /**
     * Обрабатывает событие сохранения перевода.
     */
    public void handleEvent(@NotNull TranslationSavedEventDto event) {
        if (event.isUpdated()) {
            bot.sendTextMessage(event.getChatId(), TRANSLATION_UPDATED_TEXT, event.getSaveTranslationMessageId());
        } else {
            bot.sendTextMessage(event.getChatId(), TRANSLATION_CREATED_TEXT, event.getSaveTranslationMessageId());
        }
    }

    /**
     * Обрабатывает событие задания вопроса.
     */
    public void handleEvent(@NotNull QuestionAskedEventDto event) {
        bot.sendTextMessage(event.getChatId(), format(QUESTION_ADDED_TEXT, event.getPhrase()), null);
    }

    /**
     * Обрабатывает событие правильного ответа на вопрос.
     */
    public void handleEvent(@NotNull QuestionAnsweredCorrectlyEventDto event) {
        bot.sendTextMessage(event.getChatId(), format(QUESTION_ANSWERED_CORRECTLY_TEXT, event.getPhrase()),
                event.getAnswerMessageId());
    }

    /**
     * Обрабатывает событие неправильного ответа на вопрос.
     */
    public void handleEvent(@NotNull QuestionAnsweredNotCorrectlyEventDto event) {
        bot.sendTextMessage(event.getChatId(), format(QUESTION_ANSWERED_NOT_CORRECTLY_TEXT, event.getPhrase()),
                event.getAnswerMessageId());
    }

    /**
     * Обрабатывает событие отмены вопроса.
     */
    public void handleEvent(@NotNull QuestionCancelledEventDto event) {
        bot.sendTextMessage(event.getChatId(), format(QUESTION_CANCELLED_TEXT, event.getPhrase()),
                event.getCancelMessageId());
    }
}
