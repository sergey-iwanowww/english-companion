package ru.isg.englishcompanion.common.dto.enginecommands;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AcceptAnswerCommandDto extends CommandDto {

    @NotEmpty
    private String phrase;

    public AcceptAnswerCommandDto(long chatId, int messageId, @NotEmpty String phrase) {
        super(chatId, messageId);
        this.phrase = phrase;
    }
}
