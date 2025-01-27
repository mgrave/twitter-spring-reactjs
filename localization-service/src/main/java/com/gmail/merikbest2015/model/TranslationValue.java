package com.gmail.merikbest2015.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "translation_values", indexes = {
        @Index(name = "translation_language_idx", columnList = "language_code")
})
public class TranslationValue {

    private static final String TRANSLATION_VALUE_SEQ = "translation_value_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TRANSLATION_VALUE_SEQ)
    @SequenceGenerator(name = TRANSLATION_VALUE_SEQ, sequenceName = TRANSLATION_VALUE_SEQ, initialValue = 1000, allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translation_id", nullable = false)
    private Translation translation;

    @Enumerated(EnumType.STRING)
    @Column(name = "language_code", nullable = false)
    private LanguageCode languageCode;

    @Column(name = "value", nullable = false)
    private String value;
}
