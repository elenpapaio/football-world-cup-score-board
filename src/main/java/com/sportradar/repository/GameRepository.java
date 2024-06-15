package com.sportradar.repository;

import com.sportradar.data.InMemoryDatabase;
import com.sportradar.dto.GameDto;
import com.sportradar.model.Game;
import lombok.AllArgsConstructor;

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

}
