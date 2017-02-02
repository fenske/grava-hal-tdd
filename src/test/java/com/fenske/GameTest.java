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
        GameState currentGameState = game.nextActivePlayer().makeTurn(game, 1);

        assertArrayEquals(new int[]{6,0,7,7,7,7}, currentGameState.player1Pits());
        assertEquals(1, currentGameState.player1GravaHal());
        assertArrayEquals(new int[]{7,6,6,6,6,6}, currentGameState.player2Pits());
        assertEquals(0, currentGameState.player2GravaHal());

        currentGameState = game.nextActivePlayer().makeTurn(game,1);

        assertArrayEquals(new int[]{7,0,7,7,7,7}, currentGameState.player1Pits());
        assertEquals(1, currentGameState.player1GravaHal());
        assertArrayEquals(new int[]{7,0,7,7,7,7}, currentGameState.player2Pits());
        assertEquals(1, currentGameState.player2GravaHal());

        currentGameState = game.nextActivePlayer().makeTurn(game,2);

        assertArrayEquals(new int[]{7,0,0,8,8,8}, currentGameState.player1Pits());
        assertEquals(2, currentGameState.player1GravaHal());
        assertArrayEquals(new int[]{8,1,8,7,7,7}, currentGameState.player2Pits());
        assertEquals(1, currentGameState.player2GravaHal());

        currentGameState = game.nextActivePlayer().makeTurn(game,5);

        assertArrayEquals(new int[]{8,1,1,9,9,9}, currentGameState.player1Pits());
        assertEquals(2, currentGameState.player1GravaHal());
        assertArrayEquals(new int[]{8,1,8,7,7,0}, currentGameState.player2Pits());
        assertEquals(2, currentGameState.player2GravaHal());

        currentGameState = game.nextActivePlayer().makeTurn(game,5);

        assertArrayEquals(new int[]{9,1,1,9,9,0}, currentGameState.player1Pits());
        assertEquals(3, currentGameState.player1GravaHal());
        assertArrayEquals(new int[]{9,2,9,8,8,1}, currentGameState.player2Pits());
        assertEquals(3, currentGameState.player2GravaHal());
    }
}
