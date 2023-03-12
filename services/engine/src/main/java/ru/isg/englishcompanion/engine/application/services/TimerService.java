package ru.isg.englishcompanion.engine.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimerService {

    private final QuestionService questionService;

    @Value("${engine.question.asking.auto-start.enabled:false}")
    private boolean autoStartEnabled;

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void generateQuestion() {

        if (!autoStartEnabled) {
            log.warn("Auto question asking is not enabled");
            return;
        }

        questionService.askRandomQuestion();
    }
}
