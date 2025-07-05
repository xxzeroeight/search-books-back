package com.search.books.book.controller.dto;

import lombok.Getter;

import java.util.List;

/**
 * 최상위 응답
 */
@Getter
public class GoogleBooksResponseDto
{
    private String kind;
    private int totalItems;
    private List<GoogleBookItemDto> items;
}
