package ru.isg.englishcompanion.engine.application.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isg.englishcompanion.engine.application.services.QuestionService;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final QuestionService questionService;

    /**
     * Отправляет во все чаты рандомный вопрос.
     */
    @PostMapping("/questions/random")
    public ResponseEntity<Void> sendRandomQuestion() {
        questionService.askRandomQuestion();
        return ResponseEntity.ok().build();
    }
}
