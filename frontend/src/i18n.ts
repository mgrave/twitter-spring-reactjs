import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import HttpBackend from "i18next-http-backend";
import LanguageDetector from "i18next-browser-languagedetector";

import { UI_V1_LOCALIZATION_TRANSLATIONS } from "./constants/endpoint-constants";
import { LanguageCode, TranslationResponse } from "./types/localization";

const transformTranslations = (data: TranslationResponse[]): Record<string, string> => {
    const transformed: Record<string, string> = {};
    data.forEach(item => {
        const translation = item.translationValues.find((value: any) => value.languageCode === LanguageCode.EN);
        if (translation) {
            transformed[item.translationKey] = translation.value;
        }
    });
    return transformed;
};

i18n
    .use(HttpBackend)
    .use(LanguageDetector)
    .use(initReactI18next)
    .init({
        fallbackLng: LanguageCode.EN,
        supportedLngs: Object.values(LanguageCode),
        backend: {
            reloadInterval: false,
            loadPath: UI_V1_LOCALIZATION_TRANSLATIONS,
            parse: (data) => transformTranslations(JSON.parse(data)),
        },
        interpolation: {
            escapeValue: false
        },
        react: {
            useSuspense: true
        },
    });

export default i18n;
