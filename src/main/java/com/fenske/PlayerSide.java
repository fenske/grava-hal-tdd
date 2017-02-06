package com.fenske;

public class PlayerSide {

    final int[] pits;
    int gravaHal;

    public PlayerSide() {
        this.pits = new int[]{6,6,6,6,6,6};
        this.gravaHal = 0;
    }

    public PlayerSide(int[] pits, int gravaHal) {
        this.pits = pits;
        this.gravaHal = gravaHal;
    }
}
