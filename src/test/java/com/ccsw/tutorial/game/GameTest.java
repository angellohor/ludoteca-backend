package com.ccsw.tutorial.game;

import com.ccsw.tutorial.author.AuthorService;
import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.category.CategoryService;
import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameTest {

    private static final Long EXISTS_GAME_ID = 1L;
    private static final Long NOT_EXISTS_GAME_ID = 10L;
    private static final Long EXISTS_CATEGORY_ID = 1L;
    private static final Long EXISTS_AUTHOR_ID = 1L;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    public void getExistsGameIdShouldReturnGame() {
        Game game = mock(Game.class);
        when(game.getId()).thenReturn(EXISTS_GAME_ID);
        when(gameRepository.findById(EXISTS_GAME_ID)).thenReturn(Optional.of(game));

        Game response = gameService.get(EXISTS_GAME_ID);
        assertNotNull(response);
        assertEquals(EXISTS_GAME_ID, response.getId());
    }

    @Test
    public void getNotExistsGameIdShouldReturnNull() {
        when(gameRepository.findById(NOT_EXISTS_GAME_ID)).thenReturn(Optional.empty());

        Game response = gameService.get(NOT_EXISTS_GAME_ID);

        assertNull(response);
    }

    @Test
    public void findWithoutFiltersShouldReturnAllGames() {
        List<Game> games = new ArrayList<>();
        games.add(mock(Game.class));
        when(gameRepository.findAll(any(Specification.class))).thenReturn(games);

        List<Game> response = gameService.find(null, null);

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(gameRepository).findAll(any(Specification.class));

    }

    @Test
    public void findWithFiltersShouldReturnAllGames() {
        List<Game> games = new ArrayList<>();
        games.add(mock(Game.class));
        when(gameRepository.findAll(any(Specification.class))).thenReturn(games);

        List<Game> response = gameService.find("Aventureros", EXISTS_CATEGORY_ID);

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(gameRepository).findAll(any(Specification.class));

    }

    @Test
    public void saveNotExistsGameIdShouldInsert() {
        GameDto gameDto = new GameDto();
        gameDto.setTitle("Nuevo Juego");

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(EXISTS_AUTHOR_ID);
        gameDto.setAuthor(authorDto);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTS_CATEGORY_ID);
        gameDto.setCategory(categoryDto);

        when(authorService.get(EXISTS_AUTHOR_ID)).thenReturn(new Author());
        when(categoryService.get(EXISTS_CATEGORY_ID)).thenReturn(new Category());

        gameService.save(null, gameDto);

        verify(authorService).get(EXISTS_AUTHOR_ID);
        verify(categoryService).get(EXISTS_CATEGORY_ID);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    public void saveExistsGameIdShouldUpdate() {
        GameDto gameDto = new GameDto();
        gameDto.setTitle("Nuevo Juego");

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(EXISTS_AUTHOR_ID);
        gameDto.setAuthor(authorDto);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(EXISTS_CATEGORY_ID);
        gameDto.setCategory(categoryDto);

        Game game = mock(Game.class);

        when(gameRepository.findById(EXISTS_GAME_ID)).thenReturn(Optional.of(game));
        when(authorService.get(EXISTS_AUTHOR_ID)).thenReturn(new Author());
        when(categoryService.get(EXISTS_CATEGORY_ID)).thenReturn(new Category());

        gameService.save(EXISTS_GAME_ID, gameDto);

        verify(gameRepository).findById(EXISTS_GAME_ID);
        verify(authorService).get(EXISTS_AUTHOR_ID);
        verify(categoryService).get(EXISTS_CATEGORY_ID);
        verify(gameRepository).save(game);
    }
}

