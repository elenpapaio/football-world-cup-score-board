# Football World Cup Score Board project

## General Description

This project aims to provide functionality for managing data of Football World Cup games. <br/>
In more detail, the following operations are supported via a central menu:
- 1 => start a game
- 2 => finish a game
- 3 => update the score of a game
- 4 => game summary sorted by total score

Also, there is an extra method, `printExistingGames`, which shows the games that are currently stored in the system. <br/>
This method is invoked after start, finish and update operations, for the user convenience.

## Implementation Assumptions
### Team names
The team names that can be used are specific and are defined in the `TeamName` enum. <br/>
If the user enters any different name, a validation error occurs. <br/>
The team names that can be used are the following:
- `Mexico`
- `Canada`
- `Spain`
- `Brazil`
- `Germany`
- `France`
- `Uruguay`
- `Italy`
- `Argentina`
- `Australia`

### In-memory database
In order to store the various information, an `ArrayList` is being used, that is a property of `InMemoryDatabase` class. <br/>
The `ArrayList` contains `Game` objects. <br/>
The `InMemoryDatabase` class provides a new id to each `Game` object that is inserted to the `ArrayList`.  