package ru.isg.englishcompanion.engine.application.repositories;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.isg.englishcompanion.engine.application.model.Translation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TranslationRepository extends JpaRepository<Translation, UUID> {

    @NotNull
    Optional<Translation> findByChatIdAndSourcePhrase(long chatId, @NotEmpty String sourcePhrase);

    @NotNull
    @Query(value = "select distinct chat_id from translations", nativeQuery = true)
    List<Long> findChatIdsWithTranslations();

    @NotNull
    List<Translation> findByChatId(long chatId);
}
