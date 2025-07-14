package com.search.books.book.service;

import com.search.books.book.controller.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
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
        List<IndustryIdentifierDto> identifiers = volumeInfoDto.getIndustryIdentifiers();

        String isbn10 = extractIsbn10(identifiers);
        String isbn13 = extractIsbn13(identifiers);
        String primaryIsbn = getPrimaryIsbn(identifiers);

        return BookInfoDto.builder()
                .isbn(primaryIsbn)
                .isbn10(isbn10)
                .isbn13(isbn13)
                .title(volumeInfoDto.getTitle())
                .subtitle(volumeInfoDto.getSubtitle())
                .author(getSafeAuthors(volumeInfoDto))
                .publisher(volumeInfoDto.getPublisher())
                .publishedDate(volumeInfoDto.getPublishedDate())
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

    /**
     * isbn10 추출
     */
    private String extractIsbn10(List<IndustryIdentifierDto> identifiers) {
        if (identifiers == null) return null;

        return identifiers.stream()
                .filter(id -> "ISBN_10".equals(id.getType()))
                .map(IndustryIdentifierDto::getIdentifier)
                .findFirst()
                .orElse(null);
    }

    private String extractIsbn13(List<IndustryIdentifierDto> identifiers) {
        if (identifiers == null) return null;

        return identifiers.stream()
                .filter(id -> "ISBN_13".equals(id.getType()))
                .map(IndustryIdentifierDto::getIdentifier)
                .findFirst()
                .orElse(null);
    }

    // 주 ISBN 결정 (ISBN-13 우선, 없으면 ISBN-10)
    private String getPrimaryIsbn(List<IndustryIdentifierDto> identifiers) {
        String isbn13 = extractIsbn13(identifiers);
        String isbn10 = extractIsbn10(identifiers);

        return isbn13 != null ? isbn13 : isbn10;
    }
}
