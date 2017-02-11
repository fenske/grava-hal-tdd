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

    private void assertGameState(Score score,
                                 int[] player1Pits, int player1Gravahal,
                                 int[] player2Pits, int player2Gravahal) {
        assertArrayEquals(player1Pits, score.getPlayer1Pits());
        assertEquals(player1Gravahal, score.getPlayer1GravaHal());
        assertArrayEquals(player2Pits, score.getPlayer2Pits());
        assertEquals(player2Gravahal, score.getPlayer2GravaHal());
    }

    @Test
    public void testMakeTurn() throws Exception {
        Score currentScore = game.getNextActivePlayer().makeGo(game, 1);

        assertGameState(currentScore,
            new int[]{6,0,7,7,7,7}, 1,
            new int[]{7,6,6,6,6,6}, 0);

        currentScore = game.getNextActivePlayer().makeGo(game,1);

        assertGameState(currentScore,
            new int[]{7,0,7,7,7,7}, 1,
            new int[]{7,0,7,7,7,7}, 1);

        currentScore = game.getNextActivePlayer().makeGo(game,2);

        assertGameState(currentScore,
            new int[]{7,0,0,8,8,8}, 2,
            new int[]{8,1,8,7,7,7}, 1);

        currentScore = game.getNextActivePlayer().makeGo(game,5);

        assertGameState(currentScore,
            new int[]{8,1,1,9,9,9}, 2,
            new int[]{8,1,8,7,7,0}, 2);

        currentScore = game.getNextActivePlayer().makeGo(game,5);

        assertGameState(currentScore,
            new int[]{9,2,1,9,9,0}, 3,
            new int[]{9,2,9,8,8,1}, 2);
    }

    @Test
    public void testLastStoneLandInOwnGravalHal() throws Exception {
        game.getNextActivePlayer().makeGo(game, 0);
        assertEquals(player1, game.getNextActivePlayer());
    }

    private Game initGame(int[] player1Pits, int player1GravaHal,
                          int[] player2Pits, int player2GravaHal) {
        PlayerSide player1Side = new PlayerSide(player1Pits, player1GravaHal);
        PlayerSide player2Side = new PlayerSide(player2Pits, player2GravaHal);
        Score initialScore = new Score(player1Side, player2Side);
        return new Game(initialScore, player1, player2);
    }

    @Test
    public void testLastStoneLandInEmptyPit() throws Exception {
        game = initGame(new int[]{1,0,1,1,1,1}, 0,
                        new int[]{1,1,1,1,1,1}, 0);
        Score currentScore = game.getNextActivePlayer().makeGo(game,0);

        assertGameState(currentScore,
            new int[]{0,0,1,1,1,1}, 2,
            new int[]{1,0,1,1,1,1}, 0);
    }

    @Test(expected=IllegalStateException.class)
    public void testGameOver() throws Exception {
        game = initGame(new int[]{0,0,0,0,0,1}, 0,
                        new int[]{1,1,1,1,1,1}, 0);
        Score gameScore = game.getNextActivePlayer().makeGo(game,5);

        assertTrue(game.isGameOver());
        assertEquals("Player2", game.getWinner());
        assertGameState(gameScore,
            new int[]{0,0,0,0,0,0}, 1,
            new int[]{0,0,0,0,0,0}, 6);

        game.getNextActivePlayer().makeGo(game,0);
    }

    @Test
    public void testTie() throws Exception {
        game = initGame(new int[]{0,0,0,0,0,1}, 0,
                        new int[]{0,0,0,0,0,1}, 0);
        game.getNextActivePlayer().makeGo(game,5);

        assertTrue(game.isGameOver());
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

        game.getNextActivePlayer().makeGo(game,5);
        Player activePlayer = game.getNextActivePlayer();
        assertEquals(player1, activePlayer);
        activePlayer.makeGo(game, 4);
        assertEquals(player2, game.getNextActivePlayer());
    }

    @Test
    public void shouldNotStealStonesFromYourSelf() {
        game = initGame(new int[]{1,1,1,1,1,3}, 0,
            new int[]{1,0,1,1,1,1}, 0);
        Score gameScore = game.getNextActivePlayer().makeGo(game,5);
        assertGameState(gameScore,
            new int[]{1,1,1,1,1,0}, 1,
            new int[]{2,1,1,1,1,1}, 0);
    }
}
