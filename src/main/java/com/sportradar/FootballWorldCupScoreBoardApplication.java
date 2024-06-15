package com.sportradar;

import com.sportradar.data.InMemoryDatabase;
import com.sportradar.repository.GameRepository;
import com.sportradar.service.GameService;
import com.sportradar.util.InputUtils;

public class FootballWorldCupScoreBoardApplication {

	public static void main(String[] args) {
		GameService gameService = new GameService(new GameRepository(new InMemoryDatabase()));

		boolean exit = false;

		do {
			System.out.println("Please choose an operation to perform: ");
			System.out.println("1 -> start a game");
			System.out.println("2 -> finish a game");
			System.out.println("3 -> update the score of a game");
			System.out.println("4 -> game summary sorted by total score");
			System.out.println("5 -> exit");
			int option = InputUtils.readIntFromKeyboard("");
			switch (option) {
				case 1:
					gameService.startGame();
					break;
				case 2:
					gameService.finishGame();
					break;
				case 3:
					gameService.updateScore();
					break;
				case 4:
					//todo
					break;
				case 5:
					exit = true;
					break;
				default:
					throw new RuntimeException("Invalid operation number.");
			}
		} while (!exit);
	}

}
