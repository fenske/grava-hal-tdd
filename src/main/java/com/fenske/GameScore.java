package com.fenske;

import lombok.NonNull;

class GameScore {

    final int[] player1Pits;
    final int player1GravaHal;
    final int[] player2Pits;
    final int player2GravaHal;

    GameScore(@NonNull Player player1, @NonNull Player player2) {
        this.player1Pits = player1.pits;
        this.player1GravaHal = player1.gravaHal;
        this.player2Pits = player2.pits;
        this.player2GravaHal = player2.gravaHal;
    }
}
