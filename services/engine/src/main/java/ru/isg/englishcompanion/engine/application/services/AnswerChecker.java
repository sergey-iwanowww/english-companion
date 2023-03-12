package ru.isg.englishcompanion.engine.application.services;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.isg.englishcompanion.engine.domain.model.Translation;

public interface AnswerChecker {
    boolean isAnswerCorrect(@NotEmpty String answerPhrase, @NotNull Translation translation);
}
