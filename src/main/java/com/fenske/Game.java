package com.fenske;

import lombok.NonNull;
import lombok.ToString;

import java.util.Arrays;
import java.util.stream.IntStream;

@ToString
public class Game {

    final Player player1;
    final Player player2;
    Player activePlayer;

    private boolean gameOver;

    public Game(@NonNull Player player1, @NonNull Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        activePlayer = player1;
    }

    public GameScore makeMove(int selectedPit) {
        if (gameOver) {
            throw new IllegalStateException("Game over");
        }
        if (activePlayer.name.equals(player1.name)) {
            activePlayer = moveStones(selectedPit, player1, player2);
        } else {
            activePlayer = moveStones(selectedPit, player2, player1);
        }
        if (anyoneIsOutOfStones()) {
            calculateFinalScore();
            gameOver = true;
        }
        return new GameScore(player1, player2);
    }

    private Player moveStones(int selectedPit, Player player, Player opposingPlayer) {
        return new Move(this, selectedPit, player, opposingPlayer).make();
    }

    private boolean anyoneIsOutOfStones() {
        return isPlayerOutOfStones(player1.pits) || isPlayerOutOfStones(player2.pits);
    }

    private void calculateFinalScore() {
        calculatePlayerScore(player1);
        calculatePlayerScore(player2);
    }

    private void calculatePlayerScore(Player player) {
        Arrays.stream(player.pits).forEach(stone -> player.gravaHal += stone);
        Arrays.fill(player.pits, 0);
    }

    private boolean isPlayerOutOfStones(int[] playerPits) {
        return IntStream.of(playerPits).sum() == 0;
    }

    public String getActivePlayerName() {
        return activePlayer.name;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        if (!gameOver) {
            throw new IllegalStateException("Game is not over yet");
        }
        if (player1.gravaHal > player2.gravaHal) {
            return player1.name;
        } else if (player2.gravaHal > player1.gravaHal) {
            return player2.name;
        } else {
            return "No winner. It's a tie";
        }
    }
}
