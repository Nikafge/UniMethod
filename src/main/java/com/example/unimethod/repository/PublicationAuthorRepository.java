package com.example.unimethod.repository;

import com.example.unimethod.model.Publication;
import com.example.unimethod.model.PublicationAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationAuthorRepository extends JpaRepository<PublicationAuthor, Long> {

    List<PublicationAuthor> findByPublication(Publication publication);

    void deleteByPublication(Publication publication);
}