package com.fenske;

class Player {

    final String name;
    final int[] pits;
    int gravaHal;

    Player(String name) {
        this.name = name;
        this.pits = new int[]{6,6,6,6,6,6};
        this.gravaHal = 0;
    }

    Player(String name, int[] pits, int gravaHal) {
        this.name = name;
        this.pits = pits;
        this.gravaHal = gravaHal;
    }
}
