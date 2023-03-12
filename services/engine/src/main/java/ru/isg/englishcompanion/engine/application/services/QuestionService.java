package ru.isg.englishcompanion.engine.application.services;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAnsweredNotCorrectlyEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionAskedEventDto;
import ru.isg.englishcompanion.common.dto.engineevents.QuestionCancelledEventDto;
import ru.isg.englishcompanion.engine.application.exceptions.QuestionNotFoundException;
import ru.isg.englishcompanion.engine.infrastructure.messaging.EngineEventsPublisher;
import ru.isg.englishcompanion.engine.domain.model.Question;
import ru.isg.englishcompanion.engine.domain.model.QuestionStatuses;
import ru.isg.englishcompanion.engine.domain.model.Translation;
import ru.isg.englishcompanion.engine.infrastructure.repositories.QuestionRepository;
import ru.isg.englishcompanion.engine.infrastructure.repositories.TranslationRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkState;
import static ru.isg.englishcompanion.common.utils.TransactionUtils.executeAfterCommit;

@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final EngineEventsPublisher engineEventsPublisher;
    private final TranslationRepository translationRepository;
    private final TranslationForAskingSelector translationForAskingSelector;
    private final AnswerChecker answerChecker;

    @NotNull
    @Transactional
    public Question askQuestion(@NotNull Translation translation) {

        Question question = new Question(translation);
        question = question.setAsked();

        Question questionToUse = questionRepository.save(question);

        executeAfterCommit(() ->
                engineEventsPublisher.publish(new QuestionAskedEventDto(questionToUse.getTranslation().getChatId(),
                        questionToUse.getTranslation().getSourcePhrase())));

        return questionToUse;
    }

    @NotNull
    @Transactional
    public Question processAnswer(@NotNull Question question, int answerMessageId, @NotEmpty String answeredPhrase) {

        checkState(question.getStatus() == QuestionStatuses.WAITING_FOR_ANSWER);

        Translation translation = question.getTranslation();

        if (answerChecker.isAnswerCorrect(answeredPhrase, translation)) {
            question = processCorrectAnswer(question, answerMessageId);
        } else {
            question = processNotCorrectAnswer(question, answerMessageId);
        }

        return question;
    }

    private Question processCorrectAnswer(Question question, int answerMessageId) {

        question = question.registerAnsweredCorrectly();

        Question questionToUse = questionRepository.save(question);

        executeAfterCommit(
                () -> engineEventsPublisher.publish(
                        new QuestionAnsweredCorrectlyEventDto(questionToUse.getTranslation().getChatId(),
                                answerMessageId,
                                questionToUse.getTranslation().getSourcePhrase())));

        return questionToUse;
    }

    private Question processNotCorrectAnswer(Question question, int answerMessageId) {

        question = question.registerAnsweredNotCorrectly();

        Question questionToUse = questionRepository.save(question);

        executeAfterCommit(
                () -> engineEventsPublisher.publish(
                        new QuestionAnsweredNotCorrectlyEventDto(questionToUse.getTranslation().getChatId(),
                                answerMessageId,
                                questionToUse.getTranslation().getSourcePhrase())));

        return questionToUse;
    }

    @NotNull
    @Transactional(readOnly = true)
    public Question getQuestion(@NotNull UUID id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Not found question by id = " + id));
    }

    @NotNull
    @Transactional(readOnly = true)
    public Question findWaitingForAnswerQuestion(long chatId) {
        return findWaitingForAnswerQuestionOpt(chatId)
                .orElseThrow(() -> new QuestionNotFoundException(
                        "Not found waiting for answer question by chatId = " + chatId));
    }

    private Optional<Question> findWaitingForAnswerQuestionOpt(long chatId) {
        return questionRepository.findByTranslationChatIdAndStatus(chatId, QuestionStatuses.WAITING_FOR_ANSWER);
    }

    @Transactional
    public void askRandomQuestion() {

        List<Long> chatIds = getChatIds();

        chatIds.forEach(chatId -> {

            Optional<Question> waitingForAnswerQuestionOpt = findWaitingForAnswerQuestionOpt(chatId);
            if (waitingForAnswerQuestionOpt.isPresent()) {
                log.debug("Chat {} has not answered question, skip it", chatId);
                return;
            }

            translationForAskingSelector.selectTranslation(chatId)
                    .ifPresentOrElse(
                            this::askQuestion,
                            () -> log.debug("Chat {} has no translation for asking questions, skip it", chatId));
        });
    }

    @NotNull
    private List<Long> getChatIds() {
        return translationRepository.findChatIdsWithTranslations();
    }

    @NotNull
    @Transactional
    public Question cancelQuestion(@NotNull Question question, int cancelMessageId) {

        checkState(question.getStatus() == QuestionStatuses.WAITING_FOR_ANSWER);

        question = question.changeToCancelled();

        Question questionToUse = questionRepository.save(question);

        executeAfterCommit(
                () -> engineEventsPublisher.publish(
                        new QuestionCancelledEventDto(questionToUse.getTranslation().getChatId(), cancelMessageId,
                                questionToUse.getTranslation().getSourcePhrase())));

        return questionToUse;
    }
}
