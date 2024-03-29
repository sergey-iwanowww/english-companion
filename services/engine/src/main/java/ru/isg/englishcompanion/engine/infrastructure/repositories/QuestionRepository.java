package ru.isg.englishcompanion.engine.infrastructure.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.isg.englishcompanion.engine.domain.model.Question;
import ru.isg.englishcompanion.engine.domain.model.QuestionStatuses;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    @NotNull
    Optional<Question> findByTranslationChatIdAndStatus(long chatId, @NotNull QuestionStatuses status);

    @NotNull
    List<Question> findByTranslationId(@NotNull UUID translationId);
}
