package com.search.books.book.controller.api;

import com.search.books.book.controller.dto.BookResponseDto;
import com.search.books.book.service.BookService;
import com.search.books.global.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BookController
{
    private final BookService bookService;

    /**
     * 메인 API: 이미지에서 도서 정보 추출 및 저장
     */
    @PostMapping("/api/books/images")
    public ResponseEntity<Map<String, Object>> extractAndSaveBook(@RequestParam("image") MultipartFile imageFile)
    {
        BookService.BookProcessResult result = bookService.processBookImage(imageFile);

        if (result.isSuccess()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", result.getMessage(),
                    "book", BookResponseDto.from(result.getBook()),
                    "timestamp", DateTimeUtils.nowAsStandard()
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", result.getError(),
                    "timestamp", DateTimeUtils.nowAsStandard()
            ));
        }
    }
}
