package com.search.books.book.service;

import com.search.books.book.entity.Book;
import com.search.books.book.respository.BookReposiroty;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookCacheService
{
    private final BookApiService bookApiService;

    private final BookReposiroty bookReposiroty;

    /**
     * DB조회 캐시
     */
    @Cacheable(value = "books", key = "#isbn", unless = "#result == null")
    public Optional<Book> findBookByIsbn(String isbn) {
        Optional<Book> book = bookReposiroty.findBookByPrimaryIsbn(isbn);

        return book;
    }


    /* API 호출 캐시
    @Cacheable(value = "bookInfo", key = "#isbn", unless = "#result == null")
    public BookInfoDto getBookInfoByISBN(String isbn)
    {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN이 필요합니다.");
        }

        // API 호출
        GoogleBooksResponseDto response = bookApiService.callGoogleBooksAPI(isbn);

        if (response.getTotalItems() == 0 || response.getItems() == null || response.getItems().isEmpty()) {
            return null;
        }

        // 결과를 BookInfoDto로 변환
        BookInfoDto bookInfoDto = bookApiService.convertToBookInfoDto(response.getItems().get(0).getVolumeInfo(), isbn);

        return bookInfoDto;
    }
    */
}
