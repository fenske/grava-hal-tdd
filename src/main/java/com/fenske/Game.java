package com.fenske;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Game {

    private final Player player1;
    private final Player player2;
    private Player activePlayer;

    private final PlayerSide player1Side;
    private final PlayerSide player2Side;

    private boolean isLandedInGravaHal;
    private boolean isOver;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        player1Side = new PlayerSide();
        player2Side = new PlayerSide();
    }

    public Game(GameState initialGameState, Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        player1Side = new PlayerSide(initialGameState.getPlayer1Pits(), initialGameState.getPlayer1GravaHal());
        player2Side = new PlayerSide(initialGameState.getPlayer2Pits(), initialGameState.getPlayer2GravaHal());
    }


    public Player getNextActivePlayer() {
        if (isLandedInGravaHal) {
            isLandedInGravaHal = false;
            return activePlayer;
        }
        activePlayer = player1.equals(activePlayer) ? player2 : player1;
        return activePlayer;
    }

    public GameState updateState(String name, int pit) {
        if (isOver()) {
            throw new IllegalStateException("Game over");
        }
        if (player1.getName().equals(name)) {
            moveStones(pit, player1Side, player2Side);
        } else {
            moveStones(pit, player2Side, player1Side);
        }
        if (isAnyoneOutOfStones()) {
            calculateFinalScore();
            setOver(true);
        }
        return new GameState(player1Side, player2Side);
    }

    private void calculateFinalScore() {
        calculatePlayerScore(player1Side);
        calculatePlayerScore(player2Side);
    }

    private void calculatePlayerScore(PlayerSide playerSide) {
        Arrays.stream(playerSide.pits).forEach(stone -> playerSide.gravaHal += stone);
        Arrays.fill(playerSide.pits, 0);
    }

    private void moveStones(int pickedPit, PlayerSide playerSide, PlayerSide opposingPlayerSide) {
        new Go(pickedPit, playerSide, opposingPlayerSide).make();
    }

    public boolean isOver() {
        return isOver;
    }

    private void setOver(boolean over) {
        isOver = over;
    }

    private boolean isAnyoneOutOfStones() {
        return isPlayerOutOfStones(player1Side.pits) || isPlayerOutOfStones(player2Side.pits);
    }

    private boolean isPlayerOutOfStones(int[] playerPits) {
        return IntStream.of(playerPits).sum() == 0;
    }

    public String getWinner() {
        if (!isOver()) {
            throw new IllegalStateException("Game is not over yet");
        }
        if (player1Side.gravaHal > player2Side.gravaHal) {
            return player1.getName();
        } else if (player2Side.gravaHal > player1Side.gravaHal) {
            return player2.getName();
        } else {
            return "No winner. It's a tie";
        }
    }

    private class Go {
        private PlayerSide playerSide;
        private PlayerSide opposingPlayerSide;
        private int remainingStones;
        private int currentPit;

        public Go(int selectedPit, PlayerSide playerSide, PlayerSide opposingPlayerSide) {
            this.playerSide = playerSide;
            this.opposingPlayerSide = opposingPlayerSide;
            this.remainingStones = playerSide.pits[selectedPit];
            this.currentPit = selectedPit + 1;
            playerSide.pits[selectedPit] = 0;
        }

        public void make() {
            while(isInActiveState()) {
                updatePlayerPits(playerSide);
                if (hasRemainingStones()) {
                    updateGravaHal(playerSide);
                }
                if (hasRemainingStones()) {
                    currentPit = 0;
                    updatePlayerPits(opposingPlayerSide);
                    currentPit = 0;
                }
            }
        }

        private boolean hasRemainingStones() {
            return remainingStones > 0;
        }

        private boolean isInActiveState() {
            return remainingStones > 0;
        }

        private void updatePlayerPits(PlayerSide playerSide) {
            for (; currentPit < playerSide.pits.length && remainingStones > 0; currentPit++) {
                playerSide.pits[currentPit]++;
                remainingStones--;
            }
            if (canStealStonesFromOpposingPlayer(playerSide)) {
                stealStonesFromOpposingPlayerAndUpdateGravaHal(playerSide, opposingPlayerSide);
            }
        }

        private boolean canStealStonesFromOpposingPlayer(PlayerSide playerSide) {
            return remainingStones == 0 && playerSide.pits[currentPit - 1] == 1;
        }

        private void stealStonesFromOpposingPlayerAndUpdateGravaHal(PlayerSide playerSide, PlayerSide opposingPlayerSide) {
            int lastEmptyPit = currentPit - 1;
            playerSide.gravaHal += playerSide.pits[lastEmptyPit] + opposingPlayerSide.pits[lastEmptyPit];
            playerSide.pits[lastEmptyPit] = 0;
            opposingPlayerSide.pits[lastEmptyPit] = 0;
        }

        private void updateGravaHal(PlayerSide playerSide) {
            playerSide.gravaHal++;
            remainingStones--;
            if (remainingStones == 0) {
                isLandedInGravaHal = true;
            }
        }
    }
}
