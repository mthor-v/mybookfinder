package com.mthor.booksfinder.repository;

import com.mthor.booksfinder.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT r FROM Book r WHERE r.idAPI = :id")
    Optional<Book> findByIdAPI(@Param("id") Integer id);

    @Query("SELECT b FROM Book b JOIN b.languages l WHERE l = :language")
    List<Book> findBooksByLanguage(@Param("language") String language);
}
