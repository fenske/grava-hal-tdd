package com.fenske;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Game {

    private final Player player1;
    private final Player player2;
    private Player activePlayer;

    private boolean isLandedInGravaHal;
    private boolean gameOver;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        activePlayer = player1;
    }


    private Player getNextActivePlayer() {
        if (isLandedInGravaHal) {
            isLandedInGravaHal = false;
            return activePlayer;
        }
        activePlayer = player1.equals(activePlayer) ? player2 : player1;
        return activePlayer;
    }

    public Score makeMove(int selectedPit) {
        if (gameOver) {
            throw new IllegalStateException("Game over");
        }
        if (player1.name.equals(activePlayer.name)) {
            moveStones(selectedPit, player1, player2);
        } else {
            moveStones(selectedPit, player2, player1);
        }
        if (anyoneIsOutOfStones()) {
            calculateFinalScore();
            gameOver = true;
        }
        activePlayer = getNextActivePlayer();
        return new Score(player1, player2);
    }

    private void moveStones(int pickedPit, Player player, Player opposingPlayer) {
        new Go(pickedPit, player, opposingPlayer).make();
    }

    private class Go {
        private Player player;
        private Player opposingPlayer;
        private int remainingStones;
        private int currentPit;

        public Go(int selectedPit, Player player, Player opposingPlayer) {
            this.player = player;
            this.opposingPlayer = opposingPlayer;
            this.remainingStones = player.pits[selectedPit];
            this.currentPit = selectedPit + 1;
            player.pits[selectedPit] = 0;
        }

        public void make() {
            while(hasRemainingStones()) {
                updatePits(player);
                if (canStealStonesFromOpposingPlayer(player)) {
                    stealStonesFromOpposingPlayerAndUpdateGravaHal(player, opposingPlayer);
                }
                if (hasRemainingStones()) {
                    updateGravaHal(player);
                }
                if (hasRemainingStones()) {
                    currentPit = 0;
                    updatePits(opposingPlayer);
                    currentPit = 0;
                }
            }
        }

        private boolean hasRemainingStones() {
            return remainingStones > 0;
        }

        private void updatePits(Player player) {
            for (; currentPit < player.pits.length && remainingStones > 0; currentPit++) {
                player.pits[currentPit]++;
                remainingStones--;
            }
        }

        private boolean canStealStonesFromOpposingPlayer(Player player) {
            return remainingStones == 0 && player.pits[currentPit - 1] == 1;
        }

        private void stealStonesFromOpposingPlayerAndUpdateGravaHal(Player player, Player opposingPlayer) {
            int lastEmptyPit = currentPit - 1;
            player.gravaHal += player.pits[lastEmptyPit] + opposingPlayer.pits[lastEmptyPit];
            player.pits[lastEmptyPit] = 0;
            opposingPlayer.pits[lastEmptyPit] = 0;
        }

        private void updateGravaHal(Player player) {
            player.gravaHal++;
            remainingStones--;
            if (remainingStones == 0) {
                isLandedInGravaHal = true;
            }
        }
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
