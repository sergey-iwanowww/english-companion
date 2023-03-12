package ru.isg.englishcompanion.common.dto.engineevents;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionAskedEventDto extends QuestionEventDto {

    public QuestionAskedEventDto(long chatId, @NotEmpty String phrase) {
        super(chatId, phrase);
    }
}
