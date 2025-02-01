export interface CountryCodeResponse {
    id: number;
    countryCode: string;
    phoneCode: string;
    country: string;
}

export interface GifImageResponse {
    id: number;
    title: string;
    src: string;
}

export interface LanguagesResponse {
    id: number;
    language: string;
}

export interface WallpaperResponse {
    id: number;
    src: string;
}

export interface TranslationResponse {
    id: number;
    translationKey: string;
    translationValues: TranslationValueResponse[]
}

export interface TranslationValueResponse {
    id: number;
    languageCode: LanguageCode;
    value: string;
}

export enum LanguageCode {
    EN = "EN",
    ES = "ES",
    DE = "DE",
    FR = "FR",
    IT = "IT"
}
