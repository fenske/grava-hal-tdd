package com.fenske;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    private void assertGameState(GameState gameState,
                                 int[] player1Pits, int player1Gravahal,
                                 int[] player2Pits, int player2Gravahal) {
        assertArrayEquals(player1Pits, gameState.player1Pits());
        assertEquals(player1Gravahal, gameState.player1GravaHal());
        assertArrayEquals(player2Pits, gameState.player2Pits());
        assertEquals(player2Gravahal, gameState.player2GravaHal());
    }

    @Test
    public void makeTurn() throws Exception {
        GameState currentGameState = game.nextActivePlayer().makeTurn(game, 1);

        assertGameState(currentGameState,
            new int[]{6,0,7,7,7,7}, 1,
            new int[]{7,6,6,6,6,6}, 0);

        currentGameState = game.nextActivePlayer().makeTurn(game,1);

        assertGameState(currentGameState,
            new int[]{7,0,7,7,7,7}, 1,
            new int[]{7,0,7,7,7,7}, 1);

        currentGameState = game.nextActivePlayer().makeTurn(game,2);

        assertGameState(currentGameState,
            new int[]{7,0,0,8,8,8}, 2,
            new int[]{8,1,8,7,7,7}, 1);

        currentGameState = game.nextActivePlayer().makeTurn(game,5);

        assertGameState(currentGameState,
            new int[]{8,1,1,9,9,9}, 2,
            new int[]{8,1,8,7,7,0}, 2);

        currentGameState = game.nextActivePlayer().makeTurn(game,5);

        assertGameState(currentGameState,
            new int[]{9,2,1,9,9,0}, 3,
            new int[]{9,2,9,8,8,1}, 2);
    }

    @Test
    public void lastStoneLandInOwnGravalHal() throws Exception {
        game.nextActivePlayer().makeTurn(game, 0);
        assertEquals(player1, game.nextActivePlayer());
    }

    private Game initGame(int[] player1Pits, int player1GravaHal,
                          int[] player2Pits, int player2GravaHal) {
        Side player1Side = new Side(player1Pits, player1GravaHal);
        Side player2Side = new Side(player2Pits, player2GravaHal);
        GameState initialState = new GameState(player1Side, player2Side);
        return new Game(initialState, player1, player2);
    }

    @Test
    public void lastStoneLandInEmptyPit() throws Exception {
        game = initGame(new int[]{1,0,1,1,1,1}, 0,
                        new int[]{1,1,1,1,1,1}, 0);
        GameState currentGameState = game.nextActivePlayer().makeTurn(game,0);

        assertGameState(currentGameState,
            new int[]{0,0,1,1,1,1}, 2,
            new int[]{1,0,1,1,1,1}, 0);
    }

    @Test(expected=IllegalStateException.class)
    public void gameOver() throws Exception {
        game = initGame(new int[]{0,0,0,0,0,1}, 0,
            new int[]{1,1,1,1,1,1}, 0);
        GameState resultState = game.nextActivePlayer().makeTurn(game,5);

        assertTrue(game.isOver());
        assertEquals("Player2", game.winner());
        assertGameState(resultState,
            new int[]{0,0,0,0,0,0}, 1,
            new int[]{0,0,0,0,0,0}, 6);

        game.nextActivePlayer().makeTurn(game,0);
    }

    @Test
    public void tie() throws Exception {
        game = initGame(new int[]{0,0,0,0,0,1}, 0,
            new int[]{0,0,0,0,0,1}, 0);
        game.nextActivePlayer().makeTurn(game,5);

        assertTrue(game.isOver());
        assertEquals("Tie", game.winner());
    }

    @Test(expected = IllegalStateException.class)
    public void gameNotOver() throws Exception {
        assertEquals("", game.winner());
    }

    @Test
    public void changePlayerIfLastNotInGravaHal() throws Exception {
        game = initGame(new int[]{0,0,0,0,3,1}, 0,
            new int[]{1,1,1,1,1,1}, 0);

        game.nextActivePlayer().makeTurn(game,5);
        Player activePlayer = game.nextActivePlayer();
        assertEquals(player1, activePlayer);
        activePlayer.makeTurn(game, 4);
        assertEquals(player2, game.nextActivePlayer());
    }
}
