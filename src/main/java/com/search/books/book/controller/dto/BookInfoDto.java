package com.search.books.book.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BookInfoDto
{
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishDate;
    private String description;
    private String imageUrl;
    private Integer pageCount;
    private String category;
    private String language;
}
