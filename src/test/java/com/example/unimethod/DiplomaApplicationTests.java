package com.example.unimethod;

import com.example.unimethod.repository.AuthorRepository;
import com.example.unimethod.repository.PublicationAuthorRepository;
import com.example.unimethod.repository.PublicationRepository;
import com.example.unimethod.repository.ReportRepository;
import com.example.unimethod.repository.TemplateRepository;
import com.example.unimethod.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude="
                + "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
                + "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,"
                + "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration"
})
class DiplomaApplicationTests {

    @MockitoBean
    AuthorRepository authorRepository;

    @MockitoBean
    PublicationAuthorRepository publicationAuthorRepository;

    @MockitoBean
    PublicationRepository publicationRepository;

    @MockitoBean
    ReportRepository reportRepository;

    @MockitoBean
    TemplateRepository templateRepository;

    @MockitoBean
    UserRepository userRepository;

    @Test
    void contextLoads() {
    }
}
