package com.example.diploma.repository;

import com.example.diploma.model.Publication;
import com.example.diploma.model.PublicationAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationAuthorRepository extends JpaRepository<PublicationAuthor, Long> {

    List<PublicationAuthor> findByPublication(Publication publication);

    void deleteByPublication(Publication publication);
}