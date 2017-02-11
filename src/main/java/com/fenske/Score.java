package com.fenske;

public class Score {

    private final int[] player1Pits;
    private final int player1GravaHal;
    private final int[] player2Pits;
    private final int player2GravaHal;

    public Score(Player player1, Player player2) {
        this.player1Pits = player1.pits;
        this.player1GravaHal = player1.gravaHal;
        this.player2Pits = player2.pits;
        this.player2GravaHal = player2.gravaHal;
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
