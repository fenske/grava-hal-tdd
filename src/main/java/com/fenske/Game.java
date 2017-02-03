package com.fenske;

public class Game {

    private final Player player1;
    private final Player player2;
    private Player activePlayer;

    private final Side player1Side;
    private final Side player2Side;

    private boolean isLandedInGravaHal; // TODO Get rid of this smell

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        player1Side = new Side();
        player2Side = new Side();
    }

    public Game(GameState initialState, Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        player1Side = new Side(initialState.player1Pits(), initialState.player1GravaHal());
        player2Side = new Side(initialState.player2Pits(), initialState.player2GravaHal());
    }


    public Player nextActivePlayer() {
        if (isLandedInGravaHal) {
            return activePlayer;
        }
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
        MoveState currentMoveState = new MoveState(side.pits[pickedPit], pickedPit + 1);
        side.pits[pickedPit] = 0;
        while(currentMoveState.remainingStones > 0) {
            updatePits(side, currentMoveState);
            if (currentMoveState.remainingStones == 0 && side.pits[currentMoveState.currentPit - 1] == 1) {
                stealStonesAndUpdateGravaHal(side, opposingSide, currentMoveState);
            }
            updateGravaHal(side, currentMoveState);
            if (currentMoveState.remainingStones == 0) {
                isLandedInGravaHal = true;
            } else {
                currentMoveState.currentPit = 0;
                updatePits(opposingSide, currentMoveState);
                currentMoveState.currentPit = 0;
            }
        }
    }

    private void stealStonesAndUpdateGravaHal(Side side, Side opposingSide, MoveState currentMoveState) {
        int lastEmptyPit = currentMoveState.currentPit - 1;
        side.gravaHal += side.pits[lastEmptyPit] + opposingSide.pits[lastEmptyPit];
        side.pits[lastEmptyPit] = 0;
        opposingSide.pits[lastEmptyPit] = 0;
    }

    private class MoveState {
        int remainingStones;
        int currentPit;

        public MoveState(int remainingStones, int currentPit) {
            this.remainingStones = remainingStones;
            this.currentPit = currentPit;
        }
    }

    private void updatePits(Side side, MoveState moveState) {
        for (; moveState.currentPit < side.pits.length && moveState.remainingStones > 0; moveState.currentPit++) {
            side.pits[moveState.currentPit]++;
            moveState.remainingStones--;
        }
    }

    private void updateGravaHal(Side side, MoveState moveState) {
        if (moveState.remainingStones > 0) {
            side.gravaHal++;
            moveState.remainingStones--;
        }
    }
}
