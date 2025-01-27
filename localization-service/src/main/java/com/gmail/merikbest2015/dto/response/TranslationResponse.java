package com.gmail.merikbest2015.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class TranslationResponse {
    private Long id;
    private String translationKey;
    private List<TranslationValueResponse> translationValues;
}
