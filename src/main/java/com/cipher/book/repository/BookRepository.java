package com.cipher.book.repository;

import com.cipher.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface BookRepository extends JpaRepository<Book, CriteriaBuilder.In> {
}
