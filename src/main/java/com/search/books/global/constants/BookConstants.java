package com.search.books.global.constants;

import java.util.Map;

public class BookConstants
{
    public static final String ISBN_13_PATTERN = "(?:ISBN[-\\s]*(?:13)?[-\\s]*:?[-\\s]*)?([97][89][-\\s]*(?:\\d[-\\s]*){9}\\d)";
    public static final String ISBN_10_PATTERN = "(?:ISBN[-\\s]*(?:10)?[-\\s]*:?[-\\s]*)?([0-9][-\\s]*(?:[0-9][-\\s]*){8}[0-9Xx])";

    public static final Map<String, String> CATEGORY_TRANSLATIONS = Map.ofEntries(
            Map.entry("fiction", "소설"),
            Map.entry("non-fiction", "논픽션"),
            Map.entry("science", "과학"),
            Map.entry("technology", "기술"),
            Map.entry("history", "역사"),
            Map.entry("biography", "전기"),
            Map.entry("autobiography", "자서전"),
            Map.entry("mystery", "미스터리"),
            Map.entry("romance", "로맨스"),
            Map.entry("fantasy", "판타지"),
            Map.entry("horror", "공포"),
            Map.entry("thriller", "스릴러"),
            Map.entry("adventure", "모험"),
            Map.entry("comedy", "코미디"),
            Map.entry("drama", "드라마"),
            Map.entry("poetry", "시"),
            Map.entry("philosophy", "철학"),
            Map.entry("religion", "종교"),
            Map.entry("self-help", "자기계발"),
            Map.entry("health", "건강"),
            Map.entry("cooking", "요리"),
            Map.entry("travel", "여행"),
            Map.entry("art", "예술"),
            Map.entry("music", "음악"),
            Map.entry("sports", "스포츠"),
            Map.entry("business", "비즈니스"),
            Map.entry("economics", "경제"),
            Map.entry("politics", "정치"),
            Map.entry("education", "교육"),
            Map.entry("children", "아동"),
            Map.entry("young-adult", "청소년"),
            Map.entry("textbook", "교과서"),
            Map.entry("reference", "참고서"),
            Map.entry("encyclopedia", "백과사전"),
            Map.entry("dictionary", "사전"),
            Map.entry("comic", "만화"),
            Map.entry("manga", "망가")
    );

    public static final Map<String, String> LANGUAGE_TRANSLATIONS = Map.ofEntries(
            Map.entry("korean", "한국어"),
            Map.entry("english", "영어"),
            Map.entry("japanese", "일본어"),
            Map.entry("chinese", "중국어"),
            Map.entry("spanish", "스페인어"),
            Map.entry("french", "프랑스어"),
            Map.entry("german", "독일어"),
            Map.entry("italian", "이탈리아어"),
            Map.entry("portuguese", "포르투갈어"),
            Map.entry("russian", "러시아어"),
            Map.entry("arabic", "아랍어"),
            Map.entry("hindi", "힌디어"),
            Map.entry("thai", "태국어"),
            Map.entry("vietnamese", "베트남어"),
            Map.entry("ko", "한국어"),
            Map.entry("en", "영어"),
            Map.entry("ja", "일본어"),
            Map.entry("zh", "중국어"),
            Map.entry("es", "스페인어"),
            Map.entry("fr", "프랑스어"),
            Map.entry("de", "독일어"),
            Map.entry("it", "이탈리아어"),
            Map.entry("pt", "포르투갈어"),
            Map.entry("ru", "러시아어"),
            Map.entry("ar", "아랍어"),
            Map.entry("hi", "힌디어"),
            Map.entry("th", "태국어"),
            Map.entry("vi", "베트남어")
    );
}
