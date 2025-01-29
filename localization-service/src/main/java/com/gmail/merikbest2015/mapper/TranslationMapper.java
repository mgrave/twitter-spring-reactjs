package com.gmail.merikbest2015.mapper;

import com.gmail.merikbest2015.commons.mapper.BasicMapper;
import com.gmail.merikbest2015.dto.request.TranslationRequest;
import com.gmail.merikbest2015.dto.response.TranslationResponse;
import com.gmail.merikbest2015.model.Translation;
import com.gmail.merikbest2015.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

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

    public TranslationResponse createTranslation(TranslationRequest request, BindingResult bindingResult) {
        return basicMapper.convertToResponse(translationService.createTranslation(request, bindingResult), TranslationResponse.class);
    }

    public TranslationResponse updateTranslation(String translationKey, TranslationRequest request, BindingResult bindingResult) {
        Translation translation = translationService.updateTranslation(translationKey, request, bindingResult);
        return basicMapper.convertToResponse(translation, TranslationResponse.class);
    }

    public String deleteTranslation(String translationKey) {
        return translationService.deleteTranslation(translationKey);
    }
}
