package com.search.books.book.controller.dto;

import lombok.Getter;

import java.util.List;

/**
 * 도서 상세 정보
 */
@Getter
public class VolumeInfoDto
{
    private String title;
    private String subtitle;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private Integer pageCount;
    private List<String> categories;
    private String language;
    private List<IndustryIdentifierDto> industryIdentifiers;
    private ImageLinksDto imageLinks;
    private String previewLink;
    private String infoLink;
}
