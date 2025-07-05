package com.search.books.book.service;

import com.search.books.book.controller.dto.BookInfoDto;
import com.search.books.book.controller.dto.GoogleBooksResponseDto;
import com.search.books.book.controller.dto.ImageLinksDto;
import com.search.books.book.controller.dto.VolumeInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookApiService
{
    private final WebClient webClient;
    private final String baseUrl;
    private final String apiKey;

    public BookApiService(@Value("${api.google-books.base-url}") String baseUrl,
                          @Value("${api.google-books.api-key}") String apiKey)
    {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * ISBN으로 도서 정보 조회 (단순)
     */
    public BookInfoDto getBookInfoByISBN(String isbn)
    {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN이 필요합니다.");
        }

        // API 호출
        GoogleBooksResponseDto response = callGoogleBooksAPI(isbn);

        if (response.getTotalItems() == 0 || response.getItems() == null || response.getItems().isEmpty()) {
            return null;
        }

        // 결과를 BookInfoDto로 변환
        BookInfoDto bookInfoDto = convertToBookInfoDto(response.getItems().get(0).getVolumeInfo(), isbn);

        return bookInfoDto;
    }

    /**
     * Google Books API 호출
     */
    private GoogleBooksResponseDto callGoogleBooksAPI(String isbn)
    {
        String apiUrl = "/volumes";

        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(apiUrl)
                            .queryParam("q", "isbn:" + isbn)
                            .queryParam("maxResultss", 1)
                            .queryParam("printType", "books");

                    if (apiKey != null && !apiKey.trim().isEmpty()) {
                        uriBuilder.queryParam("key", apiKey);
                    }

                    return uriBuilder.build();
                });

        return requestHeadersSpec.retrieve()
                .bodyToMono(GoogleBooksResponseDto.class)
                .timeout(Duration.ofSeconds(30))
                .block();
    }

    /**
     * Google Books API 응답을 BookInfoDto로 변환
     */
    private BookInfoDto convertToBookInfoDto(VolumeInfoDto volumeInfoDto, String isbn)
    {
        return BookInfoDto.builder()
                .isbn(isbn)
                .title(volumeInfoDto.getTitle())
                .author(getSafeAuthors(volumeInfoDto))
                .publisher(volumeInfoDto.getPublisher())
                .publishDate(parsePublishDate(volumeInfoDto.getPublishedDate()))
                .description(volumeInfoDto.getDescription())
                .imageUrl(getBestImageUrl(volumeInfoDto.getImageLinks()))
                .pageCount(volumeInfoDto.getPageCount())
                .category(getSafeCategory(volumeInfoDto.getCategories()))
                .language(volumeInfoDto.getLanguage())
                .build();
    }

    /**
     * 저자 추출
     */
    private String getSafeAuthors(VolumeInfoDto volumeInfoDto)
    {
        List<String> authors = volumeInfoDto.getAuthors();
        if (authors == null || authors.isEmpty()) {
            return null;
        }

        String authorString = String.join(", ", authors);
        if (authorString.length() > 300) {
            authorString = authorString.substring(0, 297) + "...";
        }

        return authorString;
    }

    /**
     * 출판일 파싱
     */
    private LocalDate parsePublishDate(String publishedDate)
    {
        if (publishedDate == null || publishedDate.trim().isEmpty()) {
            return null;
        }

        String cleanDate = publishedDate.trim();

        if (cleanDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return LocalDate.parse(cleanDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        // YYYY-MM 형식
        if (cleanDate.matches("\\d{4}-\\d{2}")) {
            return LocalDate.parse(cleanDate + "-01", DateTimeFormatter.ISO_LOCAL_DATE);
        }

        // YYYY 형식
        if (cleanDate.matches("\\d{4}")) {
            return LocalDate.parse(cleanDate + "-01-01", DateTimeFormatter.ISO_LOCAL_DATE);
        }

        return null;
    }

    /**
     * 이미지 URL 선택
     */
    private String getBestImageUrl(ImageLinksDto imageLinksDto)
    {
        if (imageLinksDto == null) {
            return null;
        }

        if (imageLinksDto.getLarge() != null) {
            return imageLinksDto.getLarge();
        }
        if (imageLinksDto.getMedium() != null) {
            return imageLinksDto.getMedium();
        }
        if (imageLinksDto.getThumbnail() != null) {
            return imageLinksDto.getThumbnail();
        }
        if (imageLinksDto.getSmall() != null) {
            return imageLinksDto.getSmall();
        }

        return imageLinksDto.getSmallThumbnail();
    }

    /**
     * 카테고리 추출
     */
    private String getSafeCategory(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return null;
        }

        String category = categories.get(0);
        if (category != null && category.length() > 100) {
            category = category.substring(0, 97) + "...";
        }
        return category;
    }
}
