package ru.isg.englishcompanion.telegrambot.application.bot;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.isg.englishcompanion.telegrambot.application.exceptions.SendTextMessageException;

import static java.lang.String.format;

@Component
@Validated
@Slf4j
public class EnglishCompanionBot extends TelegramLongPollingBot {

    private final UpdateHandler updateHandler;
    private final String name;

    public static final String TRANSLATION_CREATED_TEXT = "Перевод добавлен";
    public static final String TRANSLATION_UPDATED_TEXT = "Перевод обновлен";
    public static final String QUESTION_ADDED_TEXT = "\u2754 Переведите слово/фразу: \"%s\"";
    public static final String QUESTION_ANSWERED_CORRECTLY_TEXT = "\u2705 Правильный перевод слова/фразы: \"%s\"";
    public static final String QUESTION_ANSWERED_NOT_CORRECTLY_TEXT = "\u274C Неправильный перевод слова/фразы: \"%s\"";
    public static final String QUESTION_CANCELLED_TEXT = "\u2796 Отменен перевод слова/фразы: \"%s\"";

    public EnglishCompanionBot(@Value("${bot.token}") String token, @Value("${bot.name}") String name,
            UpdateHandler updateHandler) {
        super(token);
        this.name = name;
        this.updateHandler = updateHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    public void sendTranslationCreatedText(long chatId, int replyToMessageId) {
        sendTextMessage(chatId, TRANSLATION_CREATED_TEXT, replyToMessageId);
    }

    public void sendTranslationUpdatedText(long chatId, int replyToMessageId) {
        sendTextMessage(chatId, TRANSLATION_UPDATED_TEXT, replyToMessageId);
    }

    public void sendQuestionAddedText(long chatId, String phrase) {
        sendTextMessage(chatId, format(QUESTION_ADDED_TEXT, phrase), null);
    }

    public void sendQuestionAnsweredCorrectlyText(long chatId, String phrase, int answerMessageId) {
        sendTextMessage(chatId, format(QUESTION_ANSWERED_CORRECTLY_TEXT, phrase), answerMessageId);
    }

    public void sendQuestionAnsweredNotCorrectlyText(long chatId, String phrase, int answerMessageId) {
        sendTextMessage(chatId, format(QUESTION_ANSWERED_NOT_CORRECTLY_TEXT, phrase), answerMessageId);
    }

    public void sendQuestionCancelledText(long chatId, String phrase, int cancelMessageId) {
        sendTextMessage(chatId, format(QUESTION_CANCELLED_TEXT, phrase), cancelMessageId);
    }

    private void sendTextMessage(long chatId, String text, @Nullable Integer replyToMessageId) {
        SendMessage sendMessage = new SendMessage("" + chatId, text);
        sendMessage.setReplyToMessageId(replyToMessageId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException tae) {
            throw new SendTextMessageException(tae);
        }
    }
}
