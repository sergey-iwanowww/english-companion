package ru.isg.englishcompanion.telegrambot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.isg.englishcompanion.telegrambot.application.bot.EnglishCompanionBot;

@Component
@ConditionalOnProperty(name = "bot.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class BotsApiConfig {

    private final EnglishCompanionBot bot;

    @EventListener(ApplicationReadyEvent.class)
    public void initBots() {
        log.info("Try to configure bots");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
            log.info("Bots are configured successfully");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
