package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthorIT {
    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/author";

    public static final Long DELETE_AUTHOR_ID = 6L;
    public static final Long MODIFY_AUTHOR_ID = 3L;
    public static final String NEW_AUTHOR_NAME = "New Author";
    public static final String NEW_NATIONALITY = "New Nationality";

    private static final int TOTAL_AUTHORS = 6;
    private static final int PAGE_SIZE = 5;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<AuthorDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<AuthorDto>>() {
    };

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFiveResults() {
        AuthorSearchDto authorSearchDto = new AuthorSearchDto();
        authorSearchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(authorSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnLastResults() {
        int elementsCount = TOTAL_AUTHORS - PAGE_SIZE;

        AuthorSearchDto authorSearchDto = new AuthorSearchDto();
        authorSearchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(authorSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());
        assertEquals(elementsCount, response.getBody().getContent().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewAuthor() {
        long newAuthorId = TOTAL_AUTHORS + 1;
        long newAuthorSize = TOTAL_AUTHORS + 1;

        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(NEW_AUTHOR_NAME);
        authorDto.setNationality(NEW_NATIONALITY);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(authorDto), Void.class);

        AuthorSearchDto authorSearchDto = new AuthorSearchDto();
        authorSearchDto.setPageable(new PageableRequest(0, (int) newAuthorSize));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(authorSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newAuthorSize, response.getBody().getTotalElements());

        AuthorDto author = response.getBody().getContent().stream().filter(item -> item.getId().equals(newAuthorId)).findFirst().orElse(null);

        assertNotNull(author);
        assertEquals(NEW_AUTHOR_NAME, author.getName());
    }

    @Test
    public void modifyWithExistIdShouldModifyAuthor() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(NEW_AUTHOR_NAME);
        authorDto.setNationality(NEW_NATIONALITY);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_AUTHOR_ID, HttpMethod.PUT, new HttpEntity<>(authorDto), Void.class);

        AuthorSearchDto authorSearchDto = new AuthorSearchDto();
        authorSearchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(authorSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(TOTAL_AUTHORS, response.getBody().getTotalElements());

        AuthorDto author = response.getBody().getContent().stream().filter(item -> item.getId().equals(MODIFY_AUTHOR_ID)).findFirst().orElse(null);

        assertNotNull(author);
        assertEquals(NEW_AUTHOR_NAME, author.getName());
        assertEquals(NEW_NATIONALITY, author.getNationality());
    }

    @Test
    public void modifyWithNotExistIdShouldThrowException() {
        long authorId = TOTAL_AUTHORS + 1;

        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(NEW_AUTHOR_NAME);
        authorDto.setNationality(NEW_NATIONALITY);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + authorId, HttpMethod.PUT, new HttpEntity<>(authorDto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteWithExistIdShouldDeleteAuthor() {

        int newTotalAuthor = TOTAL_AUTHORS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_AUTHOR_ID, HttpMethod.DELETE, null, Void.class);

        AuthorSearchDto authorSearchDto = new AuthorSearchDto();
        authorSearchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<ResponsePage<AuthorDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(authorSearchDto), responseTypePage);

        assertNotNull(response);
        assertEquals(newTotalAuthor, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNotExistIdShouldThrowException() {
        long deleteAuthorId = TOTAL_AUTHORS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteAuthorId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
