package ru.isg.englishcompanion.common.dto.enginecommands;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SaveTranslationCommandDto extends CommandDto {

    @NotEmpty
    private String sourcePhrase;

    @NotEmpty
    private List<String> targetPhrases;

    public SaveTranslationCommandDto(long chatId, int messageId, @NotEmpty String sourcePhrase,
            @NotEmpty List<String> targetPhrases) {
        super(chatId, messageId);
        this.sourcePhrase = sourcePhrase;
        this.targetPhrases = targetPhrases;
    }
}
