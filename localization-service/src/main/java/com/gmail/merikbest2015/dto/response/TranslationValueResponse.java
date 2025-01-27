package com.gmail.merikbest2015.dto.response;

import com.gmail.merikbest2015.model.LanguageCode;
import lombok.Data;

@Data
public class TranslationValueResponse {
    private Long id;
    private LanguageCode languageCode;
    private String value;
}
