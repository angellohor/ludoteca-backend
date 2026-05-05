package com.ccsw.tutorial.game;

import com.ccsw.tutorial.game.model.Game;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long>, JpaSpecificationExecutor<Game> {
}
