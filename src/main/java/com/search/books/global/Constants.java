package com.search.books.global;

public class Constants
{
    public static final String ISBN_13_PATTERN = "(?:ISBN[-\\s]*(?:13)?[-\\s]*:?[-\\s]*)?([97][89][-\\s]*(?:\\d[-\\s]*){9}\\d)";
    public static final String ISBN_10_PATTERN = "(?:ISBN[-\\s]*(?:10)?[-\\s]*:?[-\\s]*)?([0-9][-\\s]*(?:[0-9][-\\s]*){8}[0-9Xx])";
}
