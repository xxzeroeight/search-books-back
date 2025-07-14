package com.search.books.global;

public class TranslationUtils
{
    public static String translateCategory(String category)
    {
        if (category == null || category.trim().isEmpty()) {
            return "";
        }

        String lowerCategory = category.toLowerCase();
        return Constants.CATEGORY_TRANSLATIONS.getOrDefault(lowerCategory, category);
    }

    public static String translateLanguage(String language)
    {
        if (language == null || language.trim().isEmpty()) {
            return "";
        }

        String lowerLanguage = language.toLowerCase();
        return Constants.LANGUAGE_TRANSLATIONS.getOrDefault(lowerLanguage, language);
    }
}
