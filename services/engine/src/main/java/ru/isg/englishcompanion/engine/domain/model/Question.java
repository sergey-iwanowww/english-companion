package ru.isg.englishcompanion.engine.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static ru.isg.englishcompanion.engine.domain.model.QuestionStatuses.ANSWERED_CORRECTLY;
import static ru.isg.englishcompanion.engine.domain.model.QuestionStatuses.CANCELLED;
import static ru.isg.englishcompanion.engine.domain.model.QuestionStatuses.NEW;
import static ru.isg.englishcompanion.engine.domain.model.QuestionStatuses.WAITING_FOR_ANSWER;

@Entity
@Table(name = "questions")
@NoArgsConstructor
@Getter
@Accessors(chain = true)
public class Question {

    public Question(@NotNull Translation translation) {
        this.id = UUID.randomUUID();
        this.translation = translation;
        this.createdDate = OffsetDateTime.now();
    }

    @Id
    private UUID id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "translation_id", nullable = false)
    private Translation translation;

    @NotNull
    @Column(nullable = false)
    @Enumerated(STRING)
    private QuestionStatuses status = NEW;

    @NotNull
    @Column(nullable = false)
    private OffsetDateTime createdDate;

    private OffsetDateTime firstAskedDate;

    private OffsetDateTime lastAskedDate;

    private OffsetDateTime answeredCorrectlyDate;

    private OffsetDateTime cancelledDate;

    @NotNull
    private int answerAttemptsCount = 0;

    public Question setAsked() {

        firstAskedDate = OffsetDateTime.now();
        lastAskedDate = firstAskedDate;

        status = WAITING_FOR_ANSWER;

        return this;
    }

    public Question registerAnsweredCorrectly() {

        answerAttemptsCount++;
        answeredCorrectlyDate = OffsetDateTime.now();

        status = ANSWERED_CORRECTLY;

        return this;
    }

    public Question registerAnsweredNotCorrectly() {

        answerAttemptsCount++;
        lastAskedDate = OffsetDateTime.now();

        status = WAITING_FOR_ANSWER;

        return this;
    }

    public Question changeToCancelled() {

        cancelledDate = OffsetDateTime.now();

        status = CANCELLED;

        return this;
    }
}
