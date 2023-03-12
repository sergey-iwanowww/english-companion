package ru.isg.englishcompanion.engine.application.services;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.engine.application.model.Translation;

/**
 * Created by s.ivanov on 3/12/23.
 */
@Component
@Validated
@Primary
public class ExactMatchAnswerChecker implements AnswerChecker {
    @Override
    public boolean isAnswerCorrect(@NotEmpty String answeredPhrase, @NotNull Translation translation) {
        return translation.getTargetPhrases().contains(answeredPhrase);
    }
}
