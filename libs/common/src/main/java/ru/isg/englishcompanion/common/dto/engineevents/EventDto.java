package ru.isg.englishcompanion.common.dto.engineevents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.isg.englishcompanion.common.dto.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class EventDto implements Message {

    protected long chatId;
}
