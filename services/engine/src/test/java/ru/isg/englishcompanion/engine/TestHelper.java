package ru.isg.englishcompanion.engine;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;
import ru.isg.englishcompanion.engine.application.model.Question;
import ru.isg.englishcompanion.engine.application.model.Translation;
import ru.isg.englishcompanion.engine.application.repositories.QuestionRepository;
import ru.isg.englishcompanion.engine.application.repositories.TranslationRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestHelper {

    private final TranslationRepository translationRepository;
    private final QuestionRepository questionRepository;

    public Translation prepareTranslation() {
        return new Translation(RandomUtils.nextLong(), RandomStringUtils.randomAlphabetic(10),
                List.of(
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10)));
    }

    public Translation createTranslation() {
        return translationRepository.save(prepareTranslation());
    }

    public Question prepareQuestion(Translation translation) {
        return new Question(translation);
    }

    public Question createQuestion(Translation translation) {
        return questionRepository.save(prepareQuestion(translation));
    }

    public Question createAskedQuestion(Translation translation) {
        Question question = prepareQuestion(translation);
        question = question.setAsked();
        return questionRepository.save(question);
    }
}
