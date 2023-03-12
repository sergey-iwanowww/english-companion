package ru.isg.englishcompanion.common.messaging;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.isg.englishcompanion.common.dto.Message;

public interface MessagePublisher {

    void publish(@NotEmpty String destinationName, @NotNull Message message);
}
