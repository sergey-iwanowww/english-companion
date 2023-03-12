package ru.isg.englishcompanion.engine;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.isg.englishcompanion.common.dto.Message;
import ru.isg.englishcompanion.common.messaging.MessagePublisher;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Component
@Primary
public class MockMessagePublisher implements MessagePublisher {

    private final Map<String, Queue<Message>> messagesMap = new HashMap<>();

    @Override
    public void publish(String destinationName, Message message) {
        messagesMap.computeIfAbsent(destinationName, k -> new ArrayDeque<>()).add(message);
    }

    public Message getMessage(String destinationName) {
        Queue<Message> messages = messagesMap.get(destinationName);
        return messages.remove();
    }
}
