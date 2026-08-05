package com.example.unimethod.config;

import com.example.unimethod.repository.AuthorRepository;
import com.example.unimethod.repository.PublicationAuthorRepository;
import com.example.unimethod.repository.PublicationRepository;
import com.example.unimethod.repository.ReportRepository;
import com.example.unimethod.repository.TemplateRepository;
import com.example.unimethod.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude="
                + "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
                + "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,"
                + "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration"
})
class SecurityConfigTest {

    @Autowired
    WebApplicationContext context;

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

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void loginPageIsPublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void publicationsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/publications"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void syncEndpointsRequireAdminRole() throws Exception {
        mockMvc.perform(get("/sync/dspace").with(user("teacher").roles("TEACHER")))
                .andExpect(status().isForbidden());
    }
}
