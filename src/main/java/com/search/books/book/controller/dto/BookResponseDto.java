package com.search.books.book.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.search.books.book.entity.Book;
import com.search.books.global.utils.TranslationUtils;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class BookResponseDto
{
    private Long id;
    private String isbn;
    private String title;
    private String subtitle;
    private String author;
    private String publisher;
    private String publishedDate;
    private String description;
    private String imageUrl;
    private String category;
    private int pageCount;
    private String language;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static BookResponseDto from(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .isbn(book.getPrimaryIsbn())
                .title(book.getTitle() != null ? book.getTitle() : "")
                .subtitle(book.getSubtitle() != null ? book.getSubtitle() : "")
                .author(book.getAuthor() != null ? book.getAuthor() : "")
                .publisher(book.getPublisher() != null ? book.getPublisher() : "")
                .publishedDate(book.getPublishedDate())
                .description(book.getDescription() != null ? book.getDescription() : "")
                .imageUrl(book.getImageUrl() != null ? convertToHttps(book.getImageUrl()) : "")
                .category(TranslationUtils.translateCategory(book.getCategory() != null ? book.getCategory() : ""))
                .language(TranslationUtils.translateLanguage(book.getLanguage() != null ? book.getLanguage() : ""))
                .pageCount(book.getPageCount())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    private static String convertToHttps(String url)
    {
        if (url != null && url.startsWith("http://")) {
            return url.replace("http://", "https://");
        }
        return url;
    }
}
