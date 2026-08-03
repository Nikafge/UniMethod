package com.example.diploma.repository;

import com.example.diploma.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFullNameNormalized(String fullNameNormalized);
}