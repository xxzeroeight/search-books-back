package com.search.books.book.controller.dto;

import lombok.Getter;

/**
 * 이미지 링크
 */
@Getter
public class ImageLinksDto
{
    private String smallThumbnail;
    private String thumbnail;
    private String small;
    private String medium;
    private String large;
    private String extraLarge;
}
