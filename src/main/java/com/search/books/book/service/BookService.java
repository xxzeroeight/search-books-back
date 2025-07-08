package com.search.books.book.service;

import com.search.books.book.controller.dto.BookInfoDto;
import com.search.books.book.entity.Book;
import com.search.books.book.respository.BookReposiroty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService
{
    private final OCRService ocrService;
    private final ISBNService isbnService;
    private final BookApiService bookApiService;
    private final BookReposiroty bookReposiroty;

    public BookProcessResult processBookImage(MultipartFile imageFile)
    {
        String ocrText = ocrService.extractTextFromImage(imageFile);

        String isbn = isbnService.extractISBN(ocrText);
        Optional<Book> existingBook = bookReposiroty.findBookByIsbn(isbn);
        if (existingBook.isPresent()) {
            return BookProcessResult.success(existingBook.get(), "이미 등록된 도서입니다.");
        }

        BookInfoDto bookInfoDto = bookApiService.getBookInfoByISBN(isbn);
        if (bookInfoDto == null) {
            return BookProcessResult.error("도서 정보를 찾을 수 없습니다: " + isbn);
        }

        Book savedBook = saveBook(bookInfoDto);

        return BookProcessResult.success(savedBook, "도서가 성공적으로 저장되었습니다.");
    }

    /**
     * BookInfoDto를 Book 엔티티로 변환하여 저장
     */
    private Book saveBook(BookInfoDto bookInfoDto)
    {
        Book book = Book.builder()
                .isbn(bookInfoDto.getIsbn())
                .title(bookInfoDto.getTitle())
                .author(bookInfoDto.getAuthor())
                .description(bookInfoDto.getDescription())
                .publisher(bookInfoDto.getPublisher())
                .publishedDate(bookInfoDto.getPublishDate())
                .category(bookInfoDto.getCategory())
                .pageCount(bookInfoDto.getPageCount())
                .language(bookInfoDto.getLanguage())
                .imageUrl(bookInfoDto.getImageUrl())
                .build();

        return bookReposiroty.save(book);
    }

    /**
     * ISBN으로 도서 조회
     */
    public Optional<Book> findBookByIsbn(String isbn)
    {
        return bookReposiroty.findBookByIsbn(isbn);
    }

    /**
     * 도서 처리 결과 클래스
     */
    @Getter
    public static class BookProcessResult
    {
        private final boolean success;
        private final String message;
        private final Book book;
        private final String error;

        private BookProcessResult(boolean success, String message, Book book, String error)
        {
            this.success = success;
            this.message = message;
            this.book = book;
            this.error = error;
        }

        public static BookProcessResult success(Book book, String message) {
            return new BookProcessResult(true, message, book, null);
        }

        public static BookProcessResult error(String error) {
            return new BookProcessResult(false, null, null, error);
        }
    }
}
