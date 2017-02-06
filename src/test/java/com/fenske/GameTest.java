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
    public void testNextActivePlayer() throws Exception {
        assertEquals(player1, game.getNextActivePlayer());
    }

    private void assertGameState(GameState gameState,
                                 int[] player1Pits, int player1Gravahal,
                                 int[] player2Pits, int player2Gravahal) {
        assertArrayEquals(player1Pits, gameState.getPlayer1Pits());
        assertEquals(player1Gravahal, gameState.getPlayer1GravaHal());
        assertArrayEquals(player2Pits, gameState.getPlayer2Pits());
        assertEquals(player2Gravahal, gameState.getPlayer2GravaHal());
    }

    @Test
    public void testMakeTurn() throws Exception {
        GameState currentGameState = game.getNextActivePlayer().makeTurn(game, 1);

        assertGameState(currentGameState,
            new int[]{6,0,7,7,7,7}, 1,
            new int[]{7,6,6,6,6,6}, 0);

        currentGameState = game.getNextActivePlayer().makeTurn(game,1);

        assertGameState(currentGameState,
            new int[]{7,0,7,7,7,7}, 1,
            new int[]{7,0,7,7,7,7}, 1);

        currentGameState = game.getNextActivePlayer().makeTurn(game,2);

        assertGameState(currentGameState,
            new int[]{7,0,0,8,8,8}, 2,
            new int[]{8,1,8,7,7,7}, 1);

        currentGameState = game.getNextActivePlayer().makeTurn(game,5);

        assertGameState(currentGameState,
            new int[]{8,1,1,9,9,9}, 2,
            new int[]{8,1,8,7,7,0}, 2);

        currentGameState = game.getNextActivePlayer().makeTurn(game,5);

        assertGameState(currentGameState,
            new int[]{9,2,1,9,9,0}, 3,
            new int[]{9,2,9,8,8,1}, 2);
    }

    @Test
    public void testLastStoneLandInOwnGravalHal() throws Exception {
        game.getNextActivePlayer().makeTurn(game, 0);
        assertEquals(player1, game.getNextActivePlayer());
    }

    private Game initGame(int[] player1Pits, int player1GravaHal,
                          int[] player2Pits, int player2GravaHal) {
        PlayerSide player1Side = new PlayerSide(player1Pits, player1GravaHal);
        PlayerSide player2Side = new PlayerSide(player2Pits, player2GravaHal);
        GameState initialGameState = new GameState(player1Side, player2Side);
        return new Game(initialGameState, player1, player2);
    }

    @Test
    public void testLastStoneLandInEmptyPit() throws Exception {
        game = initGame(new int[]{1,0,1,1,1,1}, 0,
                        new int[]{1,1,1,1,1,1}, 0);
        GameState currentGameState = game.getNextActivePlayer().makeTurn(game,0);

        assertGameState(currentGameState,
            new int[]{0,0,1,1,1,1}, 2,
            new int[]{1,0,1,1,1,1}, 0);
    }

    @Test(expected=IllegalStateException.class)
    public void testGameOver() throws Exception {
        game = initGame(new int[]{0,0,0,0,0,1}, 0,
                        new int[]{1,1,1,1,1,1}, 0);
        GameState gameScore = game.getNextActivePlayer().makeTurn(game,5);

        assertTrue(game.isOver());
        assertEquals("Player2", game.getWinner());
        assertGameState(gameScore,
            new int[]{0,0,0,0,0,0}, 1,
            new int[]{0,0,0,0,0,0}, 6);

        game.getNextActivePlayer().makeTurn(game,0);
    }

    @Test
    public void testTie() throws Exception {
        game = initGame(new int[]{0,0,0,0,0,1}, 0,
                        new int[]{0,0,0,0,0,1}, 0);
        game.getNextActivePlayer().makeTurn(game,5);

        assertTrue(game.isOver());
        assertEquals("No winner. It's a tie", game.getWinner());
    }

    @Test(expected = IllegalStateException.class)
    public void testGameNotOver() throws Exception {
        assertEquals("", game.getWinner());
    }

    @Test
    public void testChangePlayerIfLastNotInGravaHal() throws Exception {
        game = initGame(new int[]{0,0,0,0,3,1}, 0,
            new int[]{1,1,1,1,1,1}, 0);

        game.getNextActivePlayer().makeTurn(game,5);
        Player activePlayer = game.getNextActivePlayer();
        assertEquals(player1, activePlayer);
        activePlayer.makeTurn(game, 4);
        assertEquals(player2, game.getNextActivePlayer());
    }
}
