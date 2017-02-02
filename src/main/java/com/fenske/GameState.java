package com.fenske;

public class GameState {

    private final int[] player1Pits;
    private final int player1GravaHal;
    private final int[] player2Pits;
    private final int player2GravaHal;

    public GameState(Side side1, Side side2) {
        this.player1Pits = side1.pits;
        this.player1GravaHal = side1.gravaHal;
        this.player2Pits = side2.pits;
        this.player2GravaHal = side2.gravaHal;
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
