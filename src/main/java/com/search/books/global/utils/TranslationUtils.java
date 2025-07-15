package com.search.books.global.utils;

import com.search.books.global.constants.BookConstants;

public class TranslationUtils
{
    public static String translateCategory(String category)
    {
        if (category == null || category.trim().isEmpty()) {
            return "";
        }

        String lowerCategory = category.toLowerCase();
        return BookConstants.CATEGORY_TRANSLATIONS.getOrDefault(lowerCategory, category);
    }

    public static String translateLanguage(String language)
    {
        if (language == null || language.trim().isEmpty()) {
            return "";
        }

        String lowerLanguage = language.toLowerCase();
        return BookConstants.LANGUAGE_TRANSLATIONS.getOrDefault(lowerLanguage, language);
    }
}
