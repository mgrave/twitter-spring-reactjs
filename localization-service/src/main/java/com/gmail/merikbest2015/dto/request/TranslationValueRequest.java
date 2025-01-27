package com.gmail.merikbest2015.dto.request;

import com.gmail.merikbest2015.constants.LocalizationErrorMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TranslationValueRequest {

    @NotBlank(message = LocalizationErrorMessage.EMPTY_LANGUAGE_CODE)
    private String languageCode;

    @NotBlank(message = LocalizationErrorMessage.EMPTY_LANGUAGE_VALUE)
    private String value;
}
