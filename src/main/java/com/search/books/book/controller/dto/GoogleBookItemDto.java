package com.search.books.book.controller.dto;

import lombok.Getter;

/**
 * 개별 도서 항목
 */
@Getter
public class GoogleBookItemDto
{
    private String id;
    private VolumeInfoDto volumeInfo;
}
