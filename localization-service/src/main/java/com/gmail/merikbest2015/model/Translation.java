package com.gmail.merikbest2015.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "translations", indexes = {
        @Index(name = "translation_key_idx", columnList = "translation_key")
})
public class Translation {

    private static final String TRANSLATION_SEQ = "translation_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TRANSLATION_SEQ)
    @SequenceGenerator(name = TRANSLATION_SEQ, sequenceName = TRANSLATION_SEQ, initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "translation_key", nullable = false, unique = true)
    private String translationKey;

    @OneToMany(mappedBy = "translation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TranslationValue> translationValues = new ArrayList<>();
}
