package ru.isg.englishcompanion.common.dto.enginecommands;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CancelQuestionCommandDto extends CommandDto {

    public CancelQuestionCommandDto(long chatId, int messageId) {
        super(chatId, messageId);
    }
}
