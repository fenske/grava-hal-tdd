package com.fenske;

public class GameState {

    private final int[] player1Pits;
    private final int player1GravaHal;
    private final int[] player2Pits;
    private final int player2GravaHal;

    public GameState(int[] player1Pits, int player1GravaHal, int[] player2Pits, int player2GravaHal) {
        this.player1Pits = player1Pits;
        this.player1GravaHal = player1GravaHal;
        this.player2Pits = player2Pits;
        this.player2GravaHal = player2GravaHal;
    }

    public int[] player1Pits() {
        return player1Pits;
    }

    public int player1GravaHal() {
        return player1GravaHal;
    }

    public int[] player2Pits() {
        return player2Pits;
    }

    public int player2GravaHal() {
        return player2GravaHal;
    }
}
