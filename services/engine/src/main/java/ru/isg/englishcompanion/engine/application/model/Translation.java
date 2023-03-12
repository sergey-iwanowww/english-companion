package ru.isg.englishcompanion.engine.application.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "translations")
@NoArgsConstructor
@Getter
@Accessors(chain = true)
public class Translation {

    public Translation(long chatId, @NotEmpty String sourcePhrase, @NotEmpty List<String> targetPhrases) {
        this.id = UUID.randomUUID();
        this.chatId = chatId;
        this.sourcePhrase = sourcePhrase;
        this.targetPhrases = new ArrayList<>(targetPhrases);
    }

    @Id
    private UUID id;

    @Column(nullable = false)
    private long chatId;

    @NotEmpty
    @Column(nullable = false)
    private String sourcePhrase;

    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "translation_target_phrases",
            joinColumns = {
                    @JoinColumn(name = "translation_id")})
    @Column(name = "phrase", nullable = false)
    private List<String> targetPhrases = new ArrayList<>();

    public Translation setTargetPhrases(@NotEmpty List<String> targetPhrases) {
        this.targetPhrases.clear();
        this.targetPhrases.addAll(targetPhrases);
        return this;
    }
}
