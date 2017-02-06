package com.fenske;

public class Player {

    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public GameState makeGo(Game game, int pit) {
        return game.updateState(name, pit);
    }

    public String getName() {
        return name;
    }
}
