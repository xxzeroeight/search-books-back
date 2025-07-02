package com.search.books.book.service;

import com.search.books.global.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ISBNService
{
    private final Pattern isbn13Pattern;
    private final Pattern isbn10Pattern;

    public ISBNService()
    {
        this.isbn13Pattern = Pattern.compile(Constants.ISBN_13_PATTERN, Pattern.CASE_INSENSITIVE);
        this.isbn10Pattern = Pattern.compile(Constants.ISBN_10_PATTERN, Pattern.CASE_INSENSITIVE);
    }

    /**
     * OCR 텍스트에서 ISBN 추출 (가장 확실한 하나만 반환)
     */
    public String extractISBN(String ocrText)
    {
        if (ocrText == null || ocrText.trim().isEmpty()) {
            return null;
        }

        List<String> cadidates = extractAllPossibleISBNs(ocrText);

        if (cadidates.isEmpty()) {
            return null;
        }

        for (String isbn : cadidates) {
            if (isbn.length() == 13 && (isbn.startsWith("978") || isbn.startsWith("979"))) {
                return isbn;
            }
        }

        return cadidates.get(0);
    }

    /**
     * 모든 가능한 ISBN 후보들 추출
     */
    public List<String> extractAllPossibleISBNs(String ocrText)
    {
        List<String> foundISBNs = new ArrayList<>();

        // ISBN-13 검색
        Matcher matcher13 = isbn13Pattern.matcher(ocrText);
        while (matcher13.find()) {
            String candidateISBN = cleanISBN(matcher13.group(1));

            if (isValidISBN(candidateISBN) && !foundISBNs.contains(candidateISBN)) {
                foundISBNs.add(candidateISBN);
            }
        }

        // ISBN-10 검색
        Matcher matcher10 = isbn10Pattern.matcher(ocrText);
        while (matcher10.find()) {
            String candidateISBN = cleanISBN(matcher10.group(1));

            if (isValidISBN(candidateISBN) && !foundISBNs.contains(candidateISBN)) {
                foundISBNs.add(candidateISBN);
            }
        }

        return foundISBNs;
    }

    /**
     * ISBN에서 하이픈, 공백 제거
     */
    private String cleanISBN(String isbn)
    {
        if (isbn == null) {
            return null;
        }

        return isbn.replaceAll("[-\\s]", "");
    }

    /**
     * ISBN 유효성 검증 (체크섬 포함)
     */
    public boolean isValidISBN(String isbn)
    {
        if (isbn == null) {
            return false;
        }

        String cleanedISBN = cleanISBN(isbn);

        if (cleanedISBN.length() == 13) {
            return true;
        } else if (cleanedISBN.length() == 10) {
            return true;
        }

        return false;
    }

    /**
     * ISBN-13 체크섬 검증
     */
    private boolean isValidISBN13(String isbn)
    {
        if (!isbn.matches("\\d{13}") || !isbn.startsWith("978") || !isbn.startsWith("979")) {
            return false;
        }

        int sum = getSum13(isbn);
        int checkDigit = (10 - (sum % 10)) % 10;
        int lastDigit = Character.getNumericValue(isbn.charAt(12));

        return lastDigit == checkDigit;
    }

    /**
     * ISBN-10 체크섬 검증
     */
    private boolean isValidISBN10(String isbn)
    {
        if (!isbn.matches("\\d{9}[\\dXx]")) {
            return false;
        }

        int sum = getSum10(isbn);
        char lastDigit = isbn.charAt(9);

        int checkDigit;
        if (lastDigit == 'X' || lastDigit == 'x') {
            checkDigit = 10;
        } else {
            checkDigit = Character.getNumericValue(lastDigit);
        }

        return (sum + checkDigit) % 11 == 0;
    }

    /**
     * ISBN-10을 ISBN-13으로 변환
     */
    public String convertISBN10ToISBN13(String isbn10)
    {
        if (!isValidISBN10(isbn10)) {
            return null;
        }

        String isbn13Base = "978" + isbn10.substring(0, 9);

        int sum = getSum13(isbn13Base);
        int checkDigit = (10 - (sum % 10)) % 10;

        return isbn13Base + checkDigit;
    }

    /**
     * 컨텍스트 기반 ISBN 찾기 (정확도 향상)
     */
    public String extractISBNWithContext(String ocrText)
    {
        if (ocrText == null || ocrText.trim().isEmpty()) {
            return null;
        }

        String[] lines = ocrText.split("\n");

        for (String line : lines) {
            if (line.toUpperCase().contains("ISBN")) {
                String isbn = extractISBN(line);

                if (isbn != null) {
                    return isbn;
                }
            }
        }

        return extractISBN(ocrText);
    }

    /**
     * ISBN-13 가중합 계산
     */
    private int getSum13(String isbn13Base) {
        int sum = 0;
        for (int idx = 0; idx < 12; idx++) {
            int digit = Character.getNumericValue(isbn13Base.charAt(idx));
            sum += (idx % 2 == 0) ? digit : digit * 3;
        }
        return sum;
    }

    /**
     * ISBN-10 가중합 계산
     */
    private int getSum10(String isbn) {
        int sum = 0;
        for (int idx = 0; idx < 9; idx++) {
            int digit = Character.getNumericValue(isbn.charAt(idx));
            sum += digit * (10 - idx);
        }
        return sum;
    }
}
