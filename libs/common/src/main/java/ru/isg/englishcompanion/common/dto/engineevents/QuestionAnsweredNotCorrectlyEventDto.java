package ru.isg.englishcompanion.common.dto.engineevents;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionAnsweredNotCorrectlyEventDto extends QuestionEventDto {

    private int answerMessageId;

    public QuestionAnsweredNotCorrectlyEventDto(long chatId, int answerMessageId, @NotEmpty String phrase) {
        super(chatId, phrase);
        this.answerMessageId = answerMessageId;
    }
}
