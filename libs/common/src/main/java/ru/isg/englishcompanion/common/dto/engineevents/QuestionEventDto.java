package ru.isg.englishcompanion.common.dto.engineevents;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class QuestionEventDto extends EventDto {

    @NotEmpty
    protected String phrase;

    public QuestionEventDto(long chatId, @NotEmpty String phrase) {
        super(chatId);
        this.phrase = phrase;
    }
}
