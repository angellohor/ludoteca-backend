package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void findPageShouldReturnFirstPage() {

        AuthorSearchDto authorSearchDto = new AuthorSearchDto();

        PageableRequest pageableRequest = new PageableRequest(0, 5);
        authorSearchDto.setPageable(pageableRequest);

        when(authorRepository.findAll(pageableRequest.getPageable())).thenReturn(Page.empty());

        Page<Author> result = authorService.findPage(authorSearchDto);

        assertNotNull(result);
        verify(authorRepository).findAll(pageableRequest.getPageable());
    }

    public static final String AUTHOR_NAME = "NEW_AUTHOR";
    public static final String AUTHOR_NATIONALITY = "NEW NATIONALITY";

    @Test
    public void saveNotExistAuthorIdShouldInsert() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(AUTHOR_NAME);
        authorDto.setNationality(AUTHOR_NATIONALITY);

        ArgumentCaptor<Author> author = ArgumentCaptor.forClass(Author.class);
        authorService.save(null, authorDto);

        verify(authorRepository).save(author.capture());

        assertEquals(AUTHOR_NAME, author.getValue().getName());
        assertEquals(AUTHOR_NATIONALITY, author.getValue().getNationality());

    }

    private static final Long EXISTS_AUTHOR_ID = 1L;

    @Test
    public void saveWithExistAuthorIdShouldInsert() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(AUTHOR_NAME);
        authorDto.setNationality(AUTHOR_NATIONALITY);

        Author author = mock(Author.class);
        when(authorRepository.findById(EXISTS_AUTHOR_ID)).thenReturn(Optional.of(author));

        authorService.save(EXISTS_AUTHOR_ID, authorDto);

        verify(authorRepository).save(author);
    }

    @Test
    public void deleteExistsAuthorIdShouldDelete() throws Exception {
        Author author = mock(Author.class);
        when(authorRepository.findById(EXISTS_AUTHOR_ID)).thenReturn(Optional.of(author));

        authorService.delete(EXISTS_AUTHOR_ID);

        verify(authorRepository).deleteById(EXISTS_AUTHOR_ID);
    }
}
