package com.ccsw.tutorial.game;

import com.ccsw.tutorial.author.AuthorService;
import com.ccsw.tutorial.category.CategoryService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    AuthorService authorService;

    @Autowired
    CategoryService categoryService;

    @Override
    public Game get(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    @Override
    public List<Game> find(String title, Long idCategory) {

        GameSpecification titleSpec = new GameSpecification(new SearchCriteria("title", ":", title));
        GameSpecification categorySpec = new GameSpecification(new SearchCriteria("category.id", ":", idCategory));

        Specification<Game> spec = titleSpec.and(categorySpec);

        return this.gameRepository.findAll(spec);

    }

    @Override
    public void save(Long id, GameDto gameDto) {
        Game game;

        if (id == null) {
            game = new Game();
        } else {
            game = this.get(id);
        }

        BeanUtils.copyProperties(gameDto, game, "id", "author", "category");

        game.setAuthor(authorService.get(gameDto.getAuthor().getId()));
        game.setCategory(categoryService.get(gameDto.getCategory().getId()));

        this.gameRepository.save(game);
    }
}