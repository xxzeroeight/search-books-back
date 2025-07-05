package com.search.books.book.controller.api;

import com.search.books.book.entity.Book;
import com.search.books.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BookController
{
    private final BookService bookService;

    /**
     * 메인 API: 이미지에서 도서 정보 추출 및 저장
     */
    @PostMapping("/api/books/extract-and-save")
    public ResponseEntity<Map<String, Object>> extractAndSaveBook(@RequestParam("image") MultipartFile imageFile)
    {
        BookService.BookProcessResult result = bookService.processBookImage(imageFile);

        if (result.isSuccess()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", result.getMessage(),
                    "book", createBookResponseMap(result.getBook()),
                    "timestamp", LocalDateTime.now()
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", result.getError(),
                    "timestamp", LocalDateTime.now()
            ));
        }
    }

    /**
     * Book 엔티티를 응답용 Map으로 변환
     */
    private Map<String, Object> createBookResponseMap(Book book)
    {
        return Map.of(
                "id", book.getId(),
                "isbn", book.getIsbn(),
                "title", book.getTitle() != null ? book.getTitle() : "",
                "author", book.getAuthor() != null ? book.getAuthor() : "",
                "publisher", book.getPublisher() != null ? book.getPublisher() : "",
                "publishDate", book.getPublishedDate(),
                "description", book.getDescription() != null ? book.getDescription() : "",
                "imageUrl", book.getImageUrl() != null ? book.getImageUrl() : "",
                "createdAt", book.getCreatedAt(),
                "updatedAt", book.getUpdatedAt()
        );
    }
}
