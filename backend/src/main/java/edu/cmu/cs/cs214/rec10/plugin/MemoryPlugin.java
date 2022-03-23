package edu.cmu.cs.cs214.rec10.plugin;

import java.util.*;

import edu.cmu.cs.cs214.rec10.framework.core.GameFramework;
import edu.cmu.cs.cs214.rec10.framework.core.GamePlugin;
import edu.cmu.cs.cs214.rec10.games.Memory;

/**
 * An example Memory game plug-in.
 */
public class MemoryPlugin implements GamePlugin<Integer> {
    private static final int WIDTH = 4;
    private static final int HEIGHT = 4;

    private static final List<String> WORDS =
            List.of("Apple", "Boat", "Car", "Dog", "Eagle", "Fish", "Giraffe", "Helicopter");

    private static final String GAME_NAME = "Memory";
    private static final String UNKNOWN_SQUARE_STRING = "?";
    private static final String BLANK_SQUARE_STRING = "";
    private static final String SELECT_FIRST_CARD_MSG = "Select first card.";
    private static final String SELECT_SECOND_CARD_MSG = "Select second card.";
    private static final String MATCH_FOUND_MSG = "You found a match!  Select first card.";
    private static final String NOT_A_MATCH_MSG = "That was not a match.  Select first card.";
    private static final String PLAYER_WON_MSG = "Player %d won!";
    private static final String PLAYERS_TIED_MSG = "Players %s tied.";

    private GameFramework framework;
    private Memory<String> game;
    private int firstIndexSelected;
    private int secondIndexSelected;
    private int numberOfCardsSelected;
    private boolean matchFound;

    @Override
    public String getGameName() {
        return GAME_NAME;
    }

    @Override
    public int getGridWidth() {
        return WIDTH;
    }

    @Override
    public int getGridHeight() {
        return HEIGHT;
    }

    @Override
    public void onRegister(GameFramework f) {
        framework = f;
    }

    @Override
    public void onNewGame() {
        game = new Memory<>(2, WORDS);
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                framework.setSquare(i, j, UNKNOWN_SQUARE_STRING);
            }
        }
        numberOfCardsSelected = 0;
        firstIndexSelected = -1; // Negative values prevent processing of "previous" selections on first turn
        secondIndexSelected = -1;
        framework.setFooterText(SELECT_FIRST_CARD_MSG);
    }

    @Override
    public void onNewMove() {
        numberOfCardsSelected = 0;

    }

    private int boardPositionFor(int x, int y) {
        return (y * WIDTH) + x;
    }

    @Override
    public boolean isMoveValid(int x, int y) {
        if (numberOfCardsSelected == 1 && boardPositionFor(x, y) == firstIndexSelected) {
            return false; // Prevents re-selection of first card already selected
        }
        return game.hasItemAt(boardPositionFor(x, y));
    }

    @Override
    public boolean isMoveOver() {
        return numberOfCardsSelected > 1;
    }

    @Override
    public void onMovePlayed(int x, int y) {
        if (numberOfCardsSelected == 0) {
            if (firstIndexSelected >= 0 && secondIndexSelected >= 0) { // Avoids processing cards "selected" before first turn
                framework.setSquare(firstIndexSelected % HEIGHT, firstIndexSelected / HEIGHT,
                        matchFound ? BLANK_SQUARE_STRING : UNKNOWN_SQUARE_STRING);
                framework.setSquare(secondIndexSelected % HEIGHT, secondIndexSelected / HEIGHT,
                        matchFound ? BLANK_SQUARE_STRING : UNKNOWN_SQUARE_STRING);
            }

            firstIndexSelected = boardPositionFor(x, y);
            numberOfCardsSelected++;
            framework.setSquare(x, y, game.itemAt(firstIndexSelected));
            framework.setFooterText(SELECT_SECOND_CARD_MSG);
            return;
        }

        assert numberOfCardsSelected == 1;
        numberOfCardsSelected++;
        secondIndexSelected = boardPositionFor(x, y);
        framework.setSquare(x, y, game.itemAt(secondIndexSelected));
        matchFound = game.selectMatch(firstIndexSelected, secondIndexSelected);
        framework.setFooterText(matchFound ? MATCH_FOUND_MSG : NOT_A_MATCH_MSG);
        if (matchFound) {
            numberOfCardsSelected = 0;
        }
    }

    @Override
    public boolean isGameOver() {
        return game.isOver();
    }

    @Override
    public String getGameOverMessage() {
        List<Integer> winners = game.leaders();
        if (winners.size() == 1)
            return String.format(PLAYER_WON_MSG, winners.get(0) + 1);

        return String.format(PLAYERS_TIED_MSG, tiedString(winners));
    }

    private String tiedString(List<Integer> winners) {
        if (winners.size() == 2) {
            return String.format("%d and %d", winners.get(0) + 1, winners.get(1) + 1);
        }
        StringBuilder result = new StringBuilder();
        int lastPosition = winners.size() - 1;
        for (int i = 0; i < lastPosition; i++) {
            result.append(String.format("%d, ", winners.get(i) + 1));
        }
        result.append("and " + winners.get(lastPosition) + 1);
        return result.toString();
    }

    @Override
    public void onGameClosed() {
    }

    @Override
    public Integer currentPlayer() {
        return Integer.valueOf(game.currentPlayer() + 1);
    }
}
