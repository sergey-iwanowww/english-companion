package ru.isg.englishcompanion.engine;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import ru.isg.englishcompanion.common.dto.Message;
import ru.isg.englishcompanion.common.dto.enginecommands.AcceptAnswerCommandDto;
import ru.isg.englishcompanion.common.dto.enginecommands.CancelQuestionCommandDto;
import ru.isg.englishcompanion.common.dto.enginecommands.SaveTranslationCommandDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredNotCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionCancelledEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.TranslationSavedEventDto;
import ru.isg.englishcompanion.engine.application.exceptions.QuestionNotFoundException;
import ru.isg.englishcompanion.engine.application.messaging.EngineCommandsHandler;
import ru.isg.englishcompanion.engine.application.model.Question;
import ru.isg.englishcompanion.engine.application.model.Translation;
import ru.isg.englishcompanion.engine.application.repositories.QuestionRepository;
import ru.isg.englishcompanion.engine.application.repositories.TranslationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.isg.englishcompanion.engine.application.model.QuestionStatuses.ANSWERED_CORRECTLY;
import static ru.isg.englishcompanion.engine.application.model.QuestionStatuses.CANCELLED;
import static ru.isg.englishcompanion.engine.application.model.QuestionStatuses.WAITING_FOR_ANSWER;

@SpringBootTest
public class EngineCommandsHandlerTest {

    @Autowired
    private EngineCommandsHandler engineCommandsHandler;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private MockMessagePublisher messagePublisher;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestHelper testHelper;

    @Value("${engine.events.destination.name}")
    private String engineEventsQueueName;

    @Test
    @DisplayName("Тест обработки команды сохранения перевода")
    public void testSaveTranslationCommand() {

        SaveTranslationCommandDto command = new SaveTranslationCommandDto(
                RandomUtils.nextLong(),
                RandomUtils.nextInt(),
                RandomStringUtils.randomAlphabetic(10),
                List.of(
                        RandomStringUtils.randomAlphabetic(10)));

        // create

        engineCommandsHandler.handleCommand(command);

        Optional<Translation> translationOpt = translationRepository.findByChatIdAndSourcePhrase(
                command.getChatId(), command.getSourcePhrase());
        assertTrue(translationOpt.isPresent());

        Translation translation = translationOpt.get();
        assertEquals(command.getTargetPhrases().size(), translation.getTargetPhrases().size());
        assertEquals(command.getTargetPhrases().get(0), translation.getTargetPhrases().get(0));

        Message message = messagePublisher.getMessage(engineEventsQueueName);
        assertInstanceOf(TranslationSavedEventDto.class, message);

        TranslationSavedEventDto event = (TranslationSavedEventDto) message;

        assertEquals(command.getMessageId(), event.getSaveTranslationMessageId());
        assertEquals(command.getChatId(), event.getChatId());
        assertEquals(command.getSourcePhrase(), event.getSourcePhrase());
        assertEquals(command.getTargetPhrases().size(), event.getTargetPhrases().size());
        assertEquals(command.getTargetPhrases().get(0), event.getTargetPhrases().get(0));
        assertFalse(event.isUpdated());

        // and update

        command.setTargetPhrases(
                List.of(
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10)));

        engineCommandsHandler.handleCommand(command);

        translationOpt = translationRepository.findByChatIdAndSourcePhrase(
                command.getChatId(), command.getSourcePhrase());
        assertTrue(translationOpt.isPresent());

        translation = translationOpt.get();
        assertEquals(command.getTargetPhrases().size(), translation.getTargetPhrases().size());
        assertTrue(translation.getTargetPhrases().contains(command.getTargetPhrases().get(0)));
        assertTrue(translation.getTargetPhrases().contains(command.getTargetPhrases().get(1)));

        message = messagePublisher.getMessage(engineEventsQueueName);
        assertInstanceOf(TranslationSavedEventDto.class, message);

        event = (TranslationSavedEventDto) message;

        assertEquals(command.getMessageId(), event.getSaveTranslationMessageId());
        assertEquals(command.getChatId(), event.getChatId());
        assertEquals(command.getSourcePhrase(), event.getSourcePhrase());
        assertEquals(command.getTargetPhrases().size(), event.getTargetPhrases().size());
        assertTrue(event.getTargetPhrases().contains(command.getTargetPhrases().get(0)));
        assertTrue(event.getTargetPhrases().contains(command.getTargetPhrases().get(1)));
        assertTrue(event.isUpdated());
    }

    @Test
    @DisplayName("Тест обработки команды проверки правильного ответа")
    public void testAcceptCorrectAnswerCommand() {

        Translation translation = testHelper.createTranslation();

        Question question = testHelper.createAskedQuestion(translation);

        AcceptAnswerCommandDto command = new AcceptAnswerCommandDto(
                translation.getChatId(),
                RandomUtils.nextInt(),
                translation.getTargetPhrases().get(0));

        engineCommandsHandler.handleCommand(command);

        question = questionRepository.findById(question.getId()).get();

        assertEquals(1, question.getAnswerAttemptsCount());
        assertEquals(ANSWERED_CORRECTLY, question.getStatus());
        assertNotNull(question.getAnsweredCorrectlyDate());
        assertEquals(question.getFirstAskedDate(), question.getLastAskedDate());

        Message message = messagePublisher.getMessage(engineEventsQueueName);
        assertInstanceOf(QuestionAnsweredCorrectlyEventDto.class, message);

        QuestionAnsweredCorrectlyEventDto event = (QuestionAnsweredCorrectlyEventDto) message;
        assertEquals(command.getMessageId(), event.getAnswerMessageId());
        assertEquals(command.getChatId(), event.getChatId());
        assertEquals(translation.getSourcePhrase(), event.getPhrase());
    }

    @Test
    @DisplayName("Тест обработки команды проверки неправильного ответа")
    public void testAcceptNotCorrectAnswerCommand() {

        Translation translation = testHelper.createTranslation();

        Question question = testHelper.createAskedQuestion(translation);

        AcceptAnswerCommandDto command = new AcceptAnswerCommandDto(
                translation.getChatId(),
                RandomUtils.nextInt(),
                RandomStringUtils.randomAlphabetic(10));

        engineCommandsHandler.handleCommand(command);

        question = questionRepository.findById(question.getId()).get();

        assertEquals(1, question.getAnswerAttemptsCount());
        assertEquals(WAITING_FOR_ANSWER, question.getStatus());
        assertNull(question.getAnsweredCorrectlyDate());
        assertNotEquals(question.getFirstAskedDate(), question.getLastAskedDate());

        Message message = messagePublisher.getMessage(engineEventsQueueName);
        assertInstanceOf(QuestionAnsweredNotCorrectlyEventDto.class, message);

        QuestionAnsweredNotCorrectlyEventDto event = (QuestionAnsweredNotCorrectlyEventDto) message;
        assertEquals(command.getMessageId(), event.getAnswerMessageId());
        assertEquals(command.getChatId(), event.getChatId());
        assertEquals(translation.getSourcePhrase(), event.getPhrase());
    }

    @Test
    @DisplayName("Тест обработки команды проверки ответа на несуществующий вопрос")
    public void testAcceptAnswerToMissingQuestionCommand() {

        Translation translation = testHelper.createTranslation();

        AcceptAnswerCommandDto command = new AcceptAnswerCommandDto(
                translation.getChatId(),
                RandomUtils.nextInt(),
                RandomStringUtils.randomAlphabetic(10));

        assertThrows(QuestionNotFoundException.class, () ->
                engineCommandsHandler.handleCommand(command));
    }

    @Test
    @DisplayName("Тест обработки команды отмены вопроса")
    public void testCancelQuestionCommand() {

        Translation translation = testHelper.createTranslation();

        Question question = testHelper.createAskedQuestion(translation);

        CancelQuestionCommandDto command = new CancelQuestionCommandDto(
                translation.getChatId(),
                RandomUtils.nextInt());

        engineCommandsHandler.handleCommand(command);

        question = questionRepository.findById(question.getId()).get();

        assertEquals(CANCELLED, question.getStatus());
        assertNotNull(question.getCancelledDate());

        Message message = messagePublisher.getMessage(engineEventsQueueName);
        assertInstanceOf(QuestionCancelledEventDto.class, message);

        QuestionCancelledEventDto event = (QuestionCancelledEventDto) message;
        assertEquals(command.getMessageId(), event.getCancelMessageId());
        assertEquals(command.getChatId(), event.getChatId());
        assertEquals(translation.getSourcePhrase(), event.getPhrase());
    }

    @Test
    @DisplayName("Тест обработки команды отмены несуществующего вопроса")
    public void testCancelMissingQuestionCommand() {

        Translation translation = testHelper.createTranslation();

        CancelQuestionCommandDto command = new CancelQuestionCommandDto(
                translation.getChatId(),
                RandomUtils.nextInt());

        assertThrows(QuestionNotFoundException.class, () ->
                engineCommandsHandler.handleCommand(command));
    }
}
