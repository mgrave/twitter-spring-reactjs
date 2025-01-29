package com.gmail.merikbest2015.controller.rest;

import com.gmail.merikbest2015.commons.constants.PathConstants;
import com.gmail.merikbest2015.dto.request.TranslationRequest;
import com.gmail.merikbest2015.dto.response.TranslationResponse;
import com.gmail.merikbest2015.mapper.TranslationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    @PostMapping(PathConstants.TRANSLATION)
    public ResponseEntity<TranslationResponse> createTranslation(@Valid @RequestBody TranslationRequest request,
                                                                 BindingResult bindingResult) {
        return ResponseEntity.ok(translationMapper.createTranslation(request, bindingResult));
    }

    @PutMapping(PathConstants.TRANSLATION + PathConstants.TRANSLATION_KEY)
    public ResponseEntity<TranslationResponse> updateTranslation(@PathVariable("translationKey") String translationKey,
                                                                 @Valid @RequestBody TranslationRequest request,
                                                                 BindingResult bindingResult) {
        return ResponseEntity.ok(translationMapper.updateTranslation(translationKey, request, bindingResult));
    }

    @DeleteMapping(PathConstants.TRANSLATION + PathConstants.TRANSLATION_KEY)
    public ResponseEntity<String> deleteTranslation(@PathVariable("translationKey") String translationKey) {
        return ResponseEntity.ok(translationMapper.deleteTranslation(translationKey));
    }
}
