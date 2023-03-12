package ru.isg.englishcompanion.engine.application.services;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.engine.domain.model.Question;
import ru.isg.englishcompanion.engine.domain.model.Translation;
import ru.isg.englishcompanion.engine.infrastructure.repositories.QuestionRepository;
import ru.isg.englishcompanion.engine.infrastructure.repositories.TranslationRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static ru.isg.englishcompanion.engine.domain.model.QuestionStatuses.ANSWERED_CORRECTLY;

@Component
@Validated
@RequiredArgsConstructor
@Slf4j
public class FirstTranslationForAskingSelector implements TranslationForAskingSelector {

    private final TranslationRepository translationRepository;
    private final QuestionRepository questionRepository;
    public static final double STANDARD_DEVIATION = 33.3;

    /**
     * Осуществляет выбор перевода для задания вопроса, используя процент неправильных ответов (в случае отсутствия
     * ответов процент == 100). Вычисленный процент неправильных ответов корректируется с помощью рандомизации
     * с использованием нормального распределения.
     */
    @NotNull
    public Optional<Translation> selectTranslation(long chatId) {
        return translationRepository.findByChatId(chatId).stream()
                .map(PrioritizedTranslation::new)
                .max(Comparator.comparingInt(PrioritizedTranslation::getPriority))
                .map(PrioritizedTranslation::getTranslation);
    }

    private class PrioritizedTranslation {

        @Getter
        private Translation translation;
        @Getter
        private int priority;

        public PrioritizedTranslation(Translation translation) {

            List<Question> questions = questionRepository.findByTranslationId(translation.getId());
            int attemptsCount = 0;
            int correctlyAnsweredCount = 0;
            for (Question question : questions) {
                attemptsCount += question.getAnswerAttemptsCount();
                if (question.getStatus() == ANSWERED_CORRECTLY) {
                    correctlyAnsweredCount++;
                }
            }

            double koeff = attemptsCount == 0
                    ? 100.0
                    : (attemptsCount - correctlyAnsweredCount) * 1.0 / attemptsCount * 100.0;

            this.translation = translation;

            this.priority = (int) new Random().nextGaussian(koeff, STANDARD_DEVIATION);
            if (this.priority < 0) {
                this.priority = 0;
            } else if (this.priority > 100) {
                this.priority = 100;
            }

            log.debug("translation phrase {} has koeff == {} and priority == {}", this.translation.getSourcePhrase(),
                    koeff, this.priority);
        }
    }
}
