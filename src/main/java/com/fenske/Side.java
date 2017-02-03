package com.fenske;

public class Side {

    final int[] pits;
    int gravaHal;

    public Side() {
        this.pits = new int[]{6,6,6,6,6,6};
        this.gravaHal = 0;
    }

    public Side(int[] pits, int gravaHal) {
        this.pits = pits;
        this.gravaHal = gravaHal;
    }
}
