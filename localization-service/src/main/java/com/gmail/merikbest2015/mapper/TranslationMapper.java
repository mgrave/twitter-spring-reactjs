package com.gmail.merikbest2015.mapper;

import com.gmail.merikbest2015.commons.mapper.BasicMapper;
import com.gmail.merikbest2015.dto.response.TranslationResponse;
import com.gmail.merikbest2015.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TranslationMapper {

    private final BasicMapper basicMapper;
    private final TranslationService translationService;

    public List<TranslationResponse> getTranslations() {
        return basicMapper.convertToResponseList(translationService.getTranslations(), TranslationResponse.class);
    }

    public TranslationResponse getTranslation(String translationKey) {
        return basicMapper.convertToResponse(translationService.getTranslation(translationKey), TranslationResponse.class);
    }
}
