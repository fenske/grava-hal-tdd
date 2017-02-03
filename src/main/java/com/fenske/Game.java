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
        return new GameState(player1Side, player2Side);
    }

    private void moveStones(int pickedPit, Side side, Side opposingSide) {
        int stones = side.pits[pickedPit];
        side.pits[pickedPit] = 0;
        int currentPit = pickedPit + 1;
        while(stones > 0) {
            stones = updatePits(side, currentPit, stones);
            stones = updateGravaHal(side, stones);
            stones = updatePits(opposingSide, 0, stones);
            currentPit = 0;
        }
    }

    private int updatePits(Side side, int pickedPit, int stones) {
        for (int currentPit = pickedPit; currentPit < side.pits.length && stones > 0; currentPit++) {
            side.pits[currentPit]++;
            stones--;
        }
        return stones;
    }

    private int updateGravaHal(Side side, int stones) {
        if (stones > 0) {
            side.gravaHal++;
            stones--;
        }
        return stones;
    }
}
