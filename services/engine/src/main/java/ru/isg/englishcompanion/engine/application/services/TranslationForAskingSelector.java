package ru.isg.englishcompanion.engine.application.services;

import jakarta.validation.constraints.NotNull;
import ru.isg.englishcompanion.engine.domain.model.Translation;

import java.util.Optional;

public interface TranslationForAskingSelector {

    @NotNull
    Optional<Translation> selectTranslation(long chatId);
}
