package com.fenske;

import lombok.val;

class Move {
    private Game game;
    private Player player;
    private Player opposingPlayer;
    private int remainingStones;
    private int currentPit;
    private boolean isLandedInGravaHal;

    Move(Game game, int selectedPit, Player player, Player opposingPlayer) {
        this.game = game;
        this.player = player;
        this.opposingPlayer = opposingPlayer;
        this.remainingStones = player.pits[selectedPit];
        this.currentPit = selectedPit + 1;
        player.pits[selectedPit] = 0;
    }

    Player make() {
        while (hasRemainingStones()) {
            currentPit = updatePits(currentPit, player);
            if (canStealStonesFromOpposingPlayer(player)) {
                stealStonesFromOpposingPlayerAndUpdateGravaHal(player, opposingPlayer);
            }
            if (hasRemainingStones()) {
                updateGravaHal(player);
            }
            if (hasRemainingStones()) {
                updatePits(0, opposingPlayer);
                currentPit = 0;
            }
        }
        return getNextActivePlayer();
    }

    private boolean hasRemainingStones() {
        return remainingStones > 0;
    }

    private int updatePits(int startPit, Player player) {
        int pit = startPit;
        while (pit < player.pits.length && remainingStones > 0) {
            player.pits[pit]++;
            remainingStones--;
            pit++;
        }
        return pit;
    }

    private boolean canStealStonesFromOpposingPlayer(Player player) {
        return remainingStones == 0 && player.pits[currentPit - 1] == 1;
    }

    private void stealStonesFromOpposingPlayerAndUpdateGravaHal(Player player, Player opposingPlayer) {
        val lastEmptyPit = currentPit - 1;
        player.gravaHal += player.pits[lastEmptyPit] + opposingPlayer.pits[lastEmptyPit];
        player.pits[lastEmptyPit] = 0;
        opposingPlayer.pits[lastEmptyPit] = 0;
    }

    private void updateGravaHal(Player player) {
        player.gravaHal++;
        remainingStones--;
        if (remainingStones == 0) {
            isLandedInGravaHal = true;
        }
    }

    private Player getNextActivePlayer() {
        if (isLandedInGravaHal) {
            isLandedInGravaHal = false;
            return game.activePlayer;
        }
        game.activePlayer = game.player1.equals(game.activePlayer) ? game.player2 : game.player1;
        return game.activePlayer;
    }
}
