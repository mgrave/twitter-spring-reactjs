package com.gmail.merikbest2015.service;

import com.gmail.merikbest2015.model.Translation;

import java.util.List;

public interface TranslationService {

    List<Translation> getTranslations();

    Translation getTranslation(String translationKey);
}
