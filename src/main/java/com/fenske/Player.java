package com.fenske;

import lombok.NonNull;

class Player {

    final String name;
    final int[] pits;
    int gravaHal;

    Player(@NonNull String name) {
        this.name = name;
        this.pits = new int[]{6,6,6,6,6,6};
        this.gravaHal = 0;
    }

    Player(@NonNull String name, @NonNull int[] pits, int gravaHal) {
        this.name = name;
        this.pits = pits;
        this.gravaHal = gravaHal;
    }
}
