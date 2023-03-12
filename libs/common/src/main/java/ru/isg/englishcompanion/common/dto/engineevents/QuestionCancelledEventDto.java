package ru.isg.englishcompanion.common.dto.engineevents;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionCancelledEventDto extends QuestionEventDto {

    private int cancelMessageId;

    public QuestionCancelledEventDto(long chatId, int cancelMessageId, @NotEmpty String phrase) {
        super(chatId, phrase);
        this.cancelMessageId = cancelMessageId;
    }
}
