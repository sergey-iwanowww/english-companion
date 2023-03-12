package ru.isg.englishcompanion.common.dto.engineevents;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TranslationSavedEventDto extends EventDto {

    private int saveTranslationMessageId;
    @NotEmpty
    private String sourcePhrase;
    @NotEmpty
    private List<String> targetPhrases;
    private boolean updated;

    public TranslationSavedEventDto(long chatId, int saveTranslationMessageId, @NotEmpty String sourcePhrase,
            @NotEmpty List<String> targetPhrases, boolean updated) {
        super(chatId);
        this.saveTranslationMessageId = saveTranslationMessageId;
        this.sourcePhrase = sourcePhrase;
        this.targetPhrases = targetPhrases;
        this.updated = updated;
    }
}
