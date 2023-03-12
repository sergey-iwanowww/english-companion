package ru.isg.englishcompanion.engine.application.services;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.isg.englishcompanion.engine.domain.model.Translation;

/**
 * Created by s.ivanov on 3/12/23.
 */
@Component
@Validated
public class FuzzyMatchAnswerChecker implements AnswerChecker {

    private static final int ALLOWABLE_DISTANCE = 1;

    @Override
    public boolean isAnswerCorrect(@NotEmpty String answeredPhrase, @NotNull Translation translation) {

        String preparedAnsweredPhrase = prepareForComparing(answeredPhrase);

        return translation.getTargetPhrases().stream()
                .anyMatch(tp -> LevenshteinDistance.getDefaultInstance()
                        .apply(prepareForComparing(tp), preparedAnsweredPhrase) <= ALLOWABLE_DISTANCE);
    }

    private String prepareForComparing(String src) {
        return src
                .toLowerCase()
                .replaceAll("\\W", "");
    }
}
