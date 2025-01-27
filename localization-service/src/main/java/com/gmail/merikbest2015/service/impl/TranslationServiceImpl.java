package com.gmail.merikbest2015.service.impl;

import com.gmail.merikbest2015.commons.exception.ApiRequestException;
import com.gmail.merikbest2015.constants.LocalizationErrorMessage;
import com.gmail.merikbest2015.model.Translation;
import com.gmail.merikbest2015.repository.TranslationRepository;
import com.gmail.merikbest2015.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;

    @Override
    public List<Translation> getTranslations() {
        return translationRepository.findAll();
    }

    @Override
    public Translation getTranslation(String translationKey) {
        return translationRepository.findByTranslationKey(translationKey)
                .orElseThrow(() -> new ApiRequestException(LocalizationErrorMessage.TRANSLATION_KEY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
