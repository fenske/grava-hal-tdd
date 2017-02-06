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
        private int selectedPit;
        private PlayerSide playerSide;
        private PlayerSide opposingPlayerSide;
        private GoState goState;

        public Go(int selectedPit, PlayerSide playerSide, PlayerSide opposingPlayerSide) {
            this.selectedPit = selectedPit;
            this.playerSide = playerSide;
            this.opposingPlayerSide = opposingPlayerSide;
            goState = new GoState(playerSide.pits[selectedPit], selectedPit + 1);
        }

        public void make() {
            playerSide.pits[selectedPit] = 0;
            while(isInActiveState()) {
                updatePlayerPits(playerSide, goState);
                updateGravaHal(playerSide, goState);
                if (isOutOfStones()) {
                    isLandedInGravaHal = true;
                } else {
                    goState.currentPit = 0;
                    updatePlayerPits(opposingPlayerSide, goState);
                    goState.currentPit = 0;
                }
            }
        }

        private boolean isOutOfStones() {
            return goState.remainingStones == 0;
        }

        private boolean isInActiveState() {
            return goState.remainingStones > 0;
        }

        private void updatePlayerPits(PlayerSide playerSide, GoState goState) {
            for (; goState.currentPit < playerSide.pits.length && goState.remainingStones > 0; goState.currentPit++) {
                playerSide.pits[goState.currentPit]++;
                goState.remainingStones--;
            }
            if (canStealStonesFromOpposingPlayer(goState, playerSide)) {
                stealStonesFromOpposingPlayerAndUpdateGravaHal(playerSide, opposingPlayerSide, goState);
            }
        }

        private boolean canStealStonesFromOpposingPlayer(GoState currentGoState, PlayerSide playerSide) {
            return currentGoState.remainingStones == 0 && playerSide.pits[currentGoState.currentPit - 1] == 1;
        }

        private void stealStonesFromOpposingPlayerAndUpdateGravaHal(PlayerSide playerSide, PlayerSide opposingPlayerSide, GoState currentGoState) {
            int lastEmptyPit = currentGoState.currentPit - 1;
            playerSide.gravaHal += playerSide.pits[lastEmptyPit] + opposingPlayerSide.pits[lastEmptyPit];
            playerSide.pits[lastEmptyPit] = 0;
            opposingPlayerSide.pits[lastEmptyPit] = 0;
        }

        private void updateGravaHal(PlayerSide playerSide, GoState goState) {
            if (goState.remainingStones > 0) {
                playerSide.gravaHal++;
                goState.remainingStones--;
            }
        }

        private class GoState {
            int remainingStones;
            int currentPit;

            public GoState(int remainingStones, int currentPit) {
                this.remainingStones = remainingStones;
                this.currentPit = currentPit;
            }
        }
    }
}
