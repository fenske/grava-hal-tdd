package com.fenske;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

    private Game game;
    private Player player1;
    private Player player2;

    @Before
    public void setUp() throws Exception {
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        game = new Game(player1, player2);
    }

    @Test
    public void nextActivePlayer() throws Exception {
        assertEquals(player1, game.nextActivePlayer());
    }

    @Test
    public void makeTurn() throws Exception {
        GameState currentState = game.nextActivePlayer().makeTurn(game, 1);
        assertArrayEquals(new int[]{6,0,7,7,7,7}, currentState.player1Pits());
        assertEquals(1, currentState.player1GravaHal());
        assertArrayEquals(new int[]{7,6,6,6,6,6}, currentState.player2Pits());
        assertEquals(0, currentState.player2GravaHal());

        currentState = game.nextActivePlayer().makeTurn(game,1);
        assertArrayEquals(new int[]{7,0,7,7,7,7}, currentState.player1Pits());
        assertEquals(1, currentState.player1GravaHal());
        assertArrayEquals(new int[]{7,0,7,7,7,7}, currentState.player2Pits());
        assertEquals(1, currentState.player2GravaHal());
    }
}
