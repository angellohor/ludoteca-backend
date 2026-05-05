package com.ccsw.tutorial.game;

import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Game", description = "API of game")
@RestController
@RequestMapping(value = "/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "Find", description = "Method that returns a filtered list of games")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<GameDto> find(@RequestParam(value = "title", required = false) String title, @RequestParam(value = "idCategory", required = false) Long idCategory) {

        List<Game> games = gameService.find(title, idCategory);

        return games.stream().map(e -> modelMapper.map(e, GameDto.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Save and Update", description = "Method that creates or updates a game")
    @RequestMapping(path = { "/{id}", "" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody GameDto gameDto) {

        gameService.save(id, gameDto);
    }

}
