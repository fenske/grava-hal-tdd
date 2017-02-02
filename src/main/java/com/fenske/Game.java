package com.fenske;

public class Game {

    private final Player player1;
    private final Player player2;
    private Player activePlayer;

    private final Side player1Side = new Side();
    private final Side player2Side = new Side();

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player nextActivePlayer() {
        activePlayer = player1.equals(activePlayer) ? player2 : player1;
        return activePlayer;
    }

    public GameState updateState(String name, int pit) {
        if (player1.name().equals(name)) {
            moveStones(pit, player1Side, player2Side);
        } else {
            moveStones(pit, player2Side, player1Side);
        }
        return new GameState(player1Side.pits, player1Side.gravaHal, player2Side.pits, player2Side.gravaHal);
    }

//    private void moveStones(int pit, int[] pits, int gravaHal, int[] opposingPits) {
    private void moveStones(int pit, Side side, Side opposingSide) {
        int stones = side.pits[pit];
        side.pits[pit] = 0;
        int i = pit + 1;
        while(stones > 0 && i < 6) {
            side.pits[i]++;
            i++;
            stones--;
        }
        if (stones > 0) {
            side.gravaHal++;
            stones--;
        }
        int j = 0;
        while(stones > 0 && j < 6) {
            opposingSide.pits[j]++;
            j++;
            stones--;
        }
    }
}
