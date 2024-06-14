package com.sportradar.repository;

import com.sportradar.data.InMemoryDatabase;
import com.sportradar.model.Game;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameRepository {

    private final InMemoryDatabase inMemoryDatabase;

    public Game save(Game game) {
        return inMemoryDatabase.insertGame(game);
    }

    public Game deleteById(int gameId) {
        return inMemoryDatabase.deleteGameById(gameId);
    }

}
