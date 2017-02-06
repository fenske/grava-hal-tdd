package com.fenske;

public class GameState {

    private final int[] player1Pits;
    private final int player1GravaHal;
    private final int[] player2Pits;
    private final int player2GravaHal;

    public GameState(PlayerSide playerSide1, PlayerSide playerSide2) {
        this.player1Pits = playerSide1.pits;
        this.player1GravaHal = playerSide1.gravaHal;
        this.player2Pits = playerSide2.pits;
        this.player2GravaHal = playerSide2.gravaHal;
    }

    public int[] getPlayer1Pits() {
        return player1Pits;
    }

    public int getPlayer1GravaHal() {
        return player1GravaHal;
    }

    public int[] getPlayer2Pits() {
        return player2Pits;
    }

    public int getPlayer2GravaHal() {
        return player2GravaHal;
    }
}
