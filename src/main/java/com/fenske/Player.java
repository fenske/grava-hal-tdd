package com.fenske;

public class Player {

    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public GameState makeTurn(Game game, int pit) {
        return game.updateState(name, pit);
    }

    public String name() {
        return name;
    }
}
