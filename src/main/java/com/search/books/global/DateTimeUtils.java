package com.search.books.global;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils
{
    public static final DateTimeFormatter STANDARD_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String nowAsStandard()
    {
        return LocalDateTime.now().format(STANDARD_FORMAT);
    }
}
