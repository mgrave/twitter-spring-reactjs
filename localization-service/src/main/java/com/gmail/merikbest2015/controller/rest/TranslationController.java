package com.gmail.merikbest2015.controller.rest;

import com.gmail.merikbest2015.commons.constants.PathConstants;
import com.gmail.merikbest2015.dto.response.TranslationResponse;
import com.gmail.merikbest2015.mapper.TranslationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.UI_V1_LOCALIZATION)
public class TranslationController {

    private final TranslationMapper translationMapper;

    @GetMapping(PathConstants.TRANSLATIONS)
    public ResponseEntity<List<TranslationResponse>> getTranslations() {
        return ResponseEntity.ok(translationMapper.getTranslations());
    }

    @GetMapping(PathConstants.TRANSLATION + PathConstants.TRANSLATION_KEY)
    public ResponseEntity<TranslationResponse> getTranslation(@PathVariable("translationKey") String translationKey) {
        return ResponseEntity.ok(translationMapper.getTranslation(translationKey));
    }
}
