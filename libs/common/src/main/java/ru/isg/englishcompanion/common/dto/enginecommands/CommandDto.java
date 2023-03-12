package ru.isg.englishcompanion.common.dto.enginecommands;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.isg.englishcompanion.common.dto.Message;

@Data
@NoArgsConstructor
public abstract class CommandDto implements Message {

    protected long chatId;
    protected int messageId;

    public CommandDto(long chatId, int messageId) {
        this.chatId = chatId;
        this.messageId = messageId;
    }
}
