package com.fenske;

public class Player {

    final String name;
    final int[] pits;
    int gravaHal;

    public Player(String name) {
        this.name = name;
        this.pits = new int[]{6,6,6,6,6,6};
        this.gravaHal = 0;
    }

    public Player(String name, int[] pits, int gravaHal) {
        this.name = name;
        this.pits = pits;
        this.gravaHal = gravaHal;
    }
}
