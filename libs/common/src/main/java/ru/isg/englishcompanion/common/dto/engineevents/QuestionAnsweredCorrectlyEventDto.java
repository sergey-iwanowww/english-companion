package ru.isg.englishcompanion.common.dto.engineevents;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionAnsweredCorrectlyEventDto extends QuestionEventDto {

    private int answerMessageId;

    public QuestionAnsweredCorrectlyEventDto(long chatId, int answerMessageId, @NotEmpty String phrase) {
        super(chatId, phrase);
        this.answerMessageId = answerMessageId;
    }
}
