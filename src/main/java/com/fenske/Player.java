package com.fenske;

public class Player {

    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public Score makeGo(Game game, int pit) {
        return game.updateScore(name, pit);
    }

    public String getName() {
        return name;
    }
}
