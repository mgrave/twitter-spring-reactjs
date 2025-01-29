package com.gmail.merikbest2015.service.impl;

import com.gmail.merikbest2015.commons.exception.ApiRequestException;
import com.gmail.merikbest2015.commons.exception.InputFieldException;
import com.gmail.merikbest2015.constants.LocalizationErrorMessage;
import com.gmail.merikbest2015.constants.LocalizationSuccessMessage;
import com.gmail.merikbest2015.dto.request.TranslationRequest;
import com.gmail.merikbest2015.dto.request.TranslationValueRequest;
import com.gmail.merikbest2015.model.LanguageCode;
import com.gmail.merikbest2015.model.Translation;
import com.gmail.merikbest2015.model.TranslationValue;
import com.gmail.merikbest2015.repository.TranslationRepository;
import com.gmail.merikbest2015.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
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

    @Override
    @Transactional
    public Translation createTranslation(TranslationRequest request, BindingResult bindingResult) {
        processInputErrors(bindingResult);
        if (translationRepository.isTranslationKeyExists(request.getTranslationKey())) {
            throw new ApiRequestException(LocalizationErrorMessage.TRANSLATION_KEY_EXISTS, HttpStatus.NOT_FOUND);
        }
        Translation translation = new Translation();
        translation.setTranslationKey(request.getTranslationKey());
        List<TranslationValue> translationValues = Arrays.stream(LanguageCode.values())
                .map(languageCode -> {
                    TranslationValue translationValue = new TranslationValue();
                    translationValue.setTranslation(translation);
                    translationValue.setLanguageCode(languageCode);
                    String value = request.getTranslationValues().stream()
                            .filter(valueRequest -> languageCode.name().equals(valueRequest.getLanguageCode()))
                            .map(TranslationValueRequest::getValue)
                            .findFirst()
                            .orElse(null);
                    translationValue.setValue(value);
                    return translationValue;
                })
                .toList();
        translation.setTranslationValues(translationValues);
        return translationRepository.save(translation);
    }

    @Override
    @Transactional
    public Translation updateTranslation(String translationKey, TranslationRequest request, BindingResult bindingResult) {
        processInputErrors(bindingResult);
        Translation translation = getTranslation(translationKey);
        List<TranslationValue> translationValues = translation.getTranslationValues();
        List<TranslationValue> newTranslationValues = translationValues.stream()
                .peek(translationValue -> {
                    String value = request.getTranslationValues().stream()
                            .filter(valueRequest -> translationValue.getLanguageCode().name().equals(valueRequest.getLanguageCode()))
                            .map(TranslationValueRequest::getValue)
                            .findFirst()
                            .orElse(null);
                    translationValue.setValue(value);
                })
                .toList();
        translationValues.clear();
        translationValues.addAll(newTranslationValues);
        return translation;
    }

    @Override
    @Transactional
    public String deleteTranslation(String translationKey) {
        Translation translation = getTranslation(translationKey);
        translationRepository.delete(translation);
        return LocalizationSuccessMessage.TRANSLATION_KEY_DELETED;
    }

    private void processInputErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
    }
}
