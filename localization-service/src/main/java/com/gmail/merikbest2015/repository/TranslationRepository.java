package com.gmail.merikbest2015.repository;

import com.gmail.merikbest2015.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    Optional<Translation> findByTranslationKey(String translationKey);

    @Query("""
            SELECT CASE WHEN count(translation) > 0 THEN true ELSE false END FROM Translation translation
            WHERE translation.translationKey = :translationKey
            """)
    boolean isTranslationKeyExists(@Param("translationKey") String translationKey);
}
