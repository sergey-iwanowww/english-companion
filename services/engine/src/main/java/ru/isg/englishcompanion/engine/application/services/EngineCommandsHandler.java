package ru.isg.englishcompanion.engine.application.services;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.common.dto.enginecommands.AcceptAnswerCommandDto;
import ru.isg.englishcompanion.common.dto.enginecommands.CancelQuestionCommandDto;
import ru.isg.englishcompanion.common.dto.enginecommands.SaveTranslationCommandDto;
import ru.isg.englishcompanion.engine.domain.model.Question;
import ru.isg.englishcompanion.engine.application.services.QuestionService;
import ru.isg.englishcompanion.engine.application.services.TranslationService;

@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class EngineCommandsHandler {

    private final QuestionService questionService;
    private final TranslationService translationService;

    /**
     * Обрабатывает команду сохранения перевода.
     * Если перевод с исходной фразой отсутствует, создает новый,
     * иначе полностью обновляет список ответных фраз.
     */
    @Transactional
    public void handleCommand(@NotNull SaveTranslationCommandDto command) {
        translationService.saveTranslation(command.getChatId(), command.getSourcePhrase(), command.getTargetPhrases(),
                command.getMessageId());
    }

    /**
     * Обрабатывает команду проверки ответа, если команда поступила в тот момент,
     * когда активный вопрос отсутствует, генерирует исключение.
     */
    @Transactional
    public void handleCommand(@NotNull AcceptAnswerCommandDto command) {
        Question question = questionService.findWaitingForAnswerQuestion(command.getChatId());
        questionService.processAnswer(question, command.getMessageId(), command.getPhrase());
    }

    /**
     * Обрабатывает команду отмены вопроса, если команда поступила в тот момент,
     * когда активный вопрос отсутствует, генерирует исключение.
     */
    @Transactional
    public void handleCommand(@NotNull CancelQuestionCommandDto command) {
        Question question = questionService.findWaitingForAnswerQuestion(command.getChatId());
        questionService.cancelQuestion(question, command.getMessageId());
    }
}
