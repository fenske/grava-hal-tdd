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
            calculateGameScore();
            setOver(true);
        }
        return new GameState(player1Side, player2Side);
    }

    private void calculateGameScore() {
        calculatePlayerSide(player1Side);
        calculatePlayerSide(player2Side);
    }

    private void calculatePlayerSide(PlayerSide playerSide) {
        Arrays.stream(playerSide.pits).forEach(pit -> playerSide.gravaHal += pit);
        Arrays.fill(playerSide.pits, 0);
    }

    private void moveStones(int pickedPit, PlayerSide playerSide, PlayerSide opposingPlayerSide) {
        MoveState currentMoveState = new MoveState(playerSide.pits[pickedPit], pickedPit + 1);
        playerSide.pits[pickedPit] = 0;
        while(currentMoveState.remainingStones > 0) {
            updatePits(playerSide, currentMoveState);
            if (canSteal(currentMoveState, playerSide)) {
                stealStonesAndUpdateGravaHal(playerSide, opposingPlayerSide, currentMoveState);
            } else {
                updateGravaHal(playerSide, currentMoveState);
                if (currentMoveState.remainingStones == 0) {
                    isLandedInGravaHal = true;
                } else {
                    currentMoveState.currentPit = 0;
                    updatePits(opposingPlayerSide, currentMoveState);
                    currentMoveState.currentPit = 0;
                }
            }
        }
    }

    private boolean canSteal(MoveState currentMoveState, PlayerSide playerSide) {
        return currentMoveState.remainingStones == 0 && playerSide.pits[currentMoveState.currentPit - 1] == 1;
    }

    private void stealStonesAndUpdateGravaHal(PlayerSide playerSide, PlayerSide opposingPlayerSide, MoveState currentMoveState) {
        int lastEmptyPit = currentMoveState.currentPit - 1;
        playerSide.gravaHal += playerSide.pits[lastEmptyPit] + opposingPlayerSide.pits[lastEmptyPit];
        playerSide.pits[lastEmptyPit] = 0;
        opposingPlayerSide.pits[lastEmptyPit] = 0;
    }

    public boolean isOver() {
        return isOver;
    }

    private void setOver(boolean over) {
        isOver = over;
    }

    private boolean isAnyoneOutOfStones() {
        return IntStream.of(player1Side.pits).sum() == 0 || IntStream.of(player2Side.pits).sum() == 0;
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

    private class MoveState {
        int remainingStones;
        int currentPit;

        public MoveState(int remainingStones, int currentPit) {
            this.remainingStones = remainingStones;
            this.currentPit = currentPit;
        }
    }

    private void updatePits(PlayerSide playerSide, MoveState moveState) {
        for (; moveState.currentPit < playerSide.pits.length && moveState.remainingStones > 0; moveState.currentPit++) {
            playerSide.pits[moveState.currentPit]++;
            moveState.remainingStones--;
        }
    }

    private void updateGravaHal(PlayerSide playerSide, MoveState moveState) {
        if (moveState.remainingStones > 0) {
            playerSide.gravaHal++;
            moveState.remainingStones--;
        }
    }
}
