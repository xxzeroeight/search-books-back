package com.search.books.book.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BookInfoDto
{
    private String isbn;
    private String isbn10;
    private String isbn13;
    private String title;
    private String subtitle;
    private String author;
    private String publisher;
    private String publishedDate;
    private String description;
    private String imageUrl;
    private Integer pageCount;
    private String category;
    private String language;
}
