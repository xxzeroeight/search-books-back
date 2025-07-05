package com.search.books.book.respository;

import com.search.books.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookReposiroty extends JpaRepository<Book, Long>
{
    Optional<Book> findBookByIsbn(String isbn);
}
