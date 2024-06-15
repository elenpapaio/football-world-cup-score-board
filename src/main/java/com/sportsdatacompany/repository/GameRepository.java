package com.sportsdatacompany.repository;

import com.sportsdatacompany.data.InMemoryDatabase;
import com.sportsdatacompany.dto.GameDto;
import com.sportsdatacompany.model.Game;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class GameRepository {

    private final InMemoryDatabase inMemoryDatabase;

    public Game save(Game game) {
        return inMemoryDatabase.insertGame(game);
    }

    public Game deleteById(int gameId) {
        return inMemoryDatabase.deleteGameById(gameId);
    }

    public Optional<Game> findById(int gameId) {
        return inMemoryDatabase.findGameById(gameId);
    }

    public Game update(GameDto gameDto) {
        return inMemoryDatabase.updateGame(gameDto);
    }

    public List<Game> findAll() {
        return inMemoryDatabase.findAllGames();
    }

}
