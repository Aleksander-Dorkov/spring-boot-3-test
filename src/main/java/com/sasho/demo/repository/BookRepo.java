package com.sasho.demo.repository;

import com.sasho.demo.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    List<Book> findAllByIdIn(List<Long> ids);

    @Query("SELECT b from Book b join fetch b.user where b.id = :id")
    Book findOneWithAuthor(@Param("id") Long id);
}
