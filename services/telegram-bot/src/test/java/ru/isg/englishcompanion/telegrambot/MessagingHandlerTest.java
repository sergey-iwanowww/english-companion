package ru.isg.englishcompanion.telegrambot;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.isg.englishcompanion.common.dto.enginecommands.AcceptAnswerCommandDto;
import ru.isg.englishcompanion.common.dto.enginecommands.CancelQuestionCommandDto;
import ru.isg.englishcompanion.common.dto.enginecommands.SaveTranslationCommandDto;
import ru.isg.englishcompanion.telegrambot.application.bot.UpdateHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
public class MessagingHandlerTest {

    @Autowired
    private UpdateHandler updateHandler;

    @Autowired
    private MockMessagePublisher messagePublisher;

    @Value("${engine.commands.destination.name}")
    private String engineCommandsQueueName;

    @Test
    @DisplayName("Тест обработки update с командой сохранения перевода")
    public void testSaveTranslationUpdate() {

        Update tgUpdate = new Update();

        Message tgMessage = new Message();

        Chat tgChat = new Chat();
        tgChat.setId(RandomUtils.nextLong());

        tgMessage.setChat(tgChat);
        tgMessage.setMessageId(RandomUtils.nextInt());
        tgMessage.setText("Hello\nПривет\nЗдравствуйте");

        tgUpdate.setMessage(tgMessage);

        updateHandler.handleUpdate(tgUpdate);

        ru.isg.englishcompanion.common.dto.Message message = messagePublisher.getMessage(engineCommandsQueueName);
        assertInstanceOf(SaveTranslationCommandDto.class, message);

        SaveTranslationCommandDto command = (SaveTranslationCommandDto) message;

        assertEquals(tgMessage.getMessageId().longValue(), command.getMessageId());
        assertEquals(tgMessage.getChat().getId(), command.getChatId());

        String[] lines = tgMessage.getText().split("\\n");
        assertEquals(lines[0], command.getSourcePhrase());
        assertEquals(lines[1], command.getTargetPhrases().get(0));
        assertEquals(lines[2], command.getTargetPhrases().get(1));
    }

    @Test
    @DisplayName("Тест обработки update с командой обработки ответа")
    public void testAcceptAnswerUpdate() {

        Update tgUpdate = new Update();

        Message tgMessage = new Message();

        Chat tgChat = new Chat();
        tgChat.setId(RandomUtils.nextLong());

        tgMessage.setChat(tgChat);
        tgMessage.setMessageId(RandomUtils.nextInt());
        tgMessage.setText("Привет");

        tgUpdate.setMessage(tgMessage);

        updateHandler.handleUpdate(tgUpdate);

        ru.isg.englishcompanion.common.dto.Message message = messagePublisher.getMessage(engineCommandsQueueName);
        assertInstanceOf(AcceptAnswerCommandDto.class, message);

        AcceptAnswerCommandDto command = (AcceptAnswerCommandDto) message;

        assertEquals(tgMessage.getMessageId().longValue(), command.getMessageId());
        assertEquals(tgMessage.getChat().getId(), command.getChatId());
        assertEquals(tgMessage.getText(), command.getPhrase());
    }

    @Test
    @DisplayName("Тест обработки update с командой отмены перевода")
    public void testCancelQuestionUpdate() {

        Update tgUpdate = new Update();

        Message tgMessage = new Message();

        Chat tgChat = new Chat();
        tgChat.setId(RandomUtils.nextLong());

        tgMessage.setChat(tgChat);
        tgMessage.setMessageId(RandomUtils.nextInt());
        tgMessage.setText("-");

        tgUpdate.setMessage(tgMessage);

        updateHandler.handleUpdate(tgUpdate);

        ru.isg.englishcompanion.common.dto.Message message = messagePublisher.getMessage(engineCommandsQueueName);
        assertInstanceOf(CancelQuestionCommandDto.class, message);

        CancelQuestionCommandDto command = (CancelQuestionCommandDto) message;

        assertEquals(tgMessage.getMessageId().longValue(), command.getMessageId());
        assertEquals(tgMessage.getChat().getId(), command.getChatId());
    }
}
