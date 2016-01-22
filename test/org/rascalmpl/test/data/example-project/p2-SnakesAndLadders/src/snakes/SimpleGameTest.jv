package snakes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ch.unibe.jexample.JExample;
import ch.unibe.jexample.Given;
import org.junit.Test;

import org.junit.runner.RunWith;

@RunWith(JExample.class)
public class SimpleGameTest {
	
	private Player jack;
	private Player jill;

	@Test
	public Game newGame() {
		jack = new Player("Jack");
		jill = new Player("Jill");
		Player[] args = { jack, jill };
		Game game = new Game(12, args);
		game.setSquareToLadder(2, 4);
		game.setSquareToLadder(7, 2);
		game.setSquareToSnake(11, -6);
		assertTrue(game.notOver());
		assertTrue(game.firstSquare().isOccupied());
		assertEquals(1, jack.position());
		assertEquals(1, jill.position());
		assertEquals(jack, game.currentPlayer());
		return game;
	}

	@Given("newGame")
	public Game initialStrings(Game game) {
		assertEquals("Jack", jack.toString());
		assertEquals("Jill", jill.toString());
		assertEquals("[1<Jack><Jill>]", game.firstSquare().toString());
		assertEquals("[2->6]", game.getSquare(2).toString());
		assertEquals("[5<-11]", game.getSquare(11).toString());
		assertEquals("[1<Jack><Jill>][2->6][3][4][5][6][7->9][8][9][10][5<-11][12]", game.toString());
		return game;
	}
	
	@Given("newGame")
	public Game move1jack(Game game) {
		game.movePlayer(4);
		assertTrue(game.notOver());
		assertEquals(5, jack.position());
		assertEquals(1, jill.position());
		assertEquals(jill, game.currentPlayer());
		return game;
	}

	@Given("move1jack")
	public Game move1strings(Game game) {
		assertEquals("[1<Jill>]", game.firstSquare().toString());
		assertEquals("[5<Jack>]", game.getSquare(5).toString());
		return game;
	}

	@Given("move1jack")
	public Game move2jackBackwards(Game game) {
		jack.moveForward(7+11); // move to end and back to start
		assertEquals(1, jack.position());
		assertEquals("[1<Jill><Jack>]", game.firstSquare().toString());
		return game;
	}

	@Given("move1jack")
	public Game move2jillLadder(Game game) {
		game.movePlayer(1);
		assertTrue(game.notOver());
		assertEquals(5, jack.position());
		assertEquals(6, jill.position());
		assertEquals(jack, game.currentPlayer());
		return game;
	}
	
	@Given("move2jillLadder")
	public Game move3jackMeetsJill(Game game) {
		assertTrue(game.getSquare(5).isOccupied());
		game.movePlayer(1);
		assertTrue(!game.getSquare(5).isOccupied());
		assertTrue(game.notOver());
		assertEquals(1, jack.position());
		assertEquals(6, jill.position());
		assertEquals(jill, game.currentPlayer());
		return game;
	}

	@Given("move3jackMeetsJill")
	public Game move4jillSnake(Game game) {
		game.movePlayer(5);
		assertTrue(game.notOver());
		assertEquals(1, jack.position());
		assertEquals(5, jill.position());
		assertEquals(jack, game.currentPlayer());
		return game;
	}

	@Given("move4jillSnake")
	public Game move5jackLadder(Game game) {
		game.movePlayer(6);
		assertTrue(game.notOver());
		assertEquals(9, jack.position());
		assertEquals(5, jill.position());
		assertEquals(jill, game.currentPlayer());
		return game;
	}
	
	@Given("move5jackLadder")
	public Game move6jill(Game game) {
		game.movePlayer(5);
		assertTrue(game.notOver());
		assertEquals(9, jack.position());
		assertEquals(10, jill.position());
		assertEquals(jack, game.currentPlayer());
		return game;
	}
	
	@Given("move6jill")
	public Game move7jackBouncesBackToJill(Game game) {
		game.movePlayer(5);
		assertTrue(game.notOver());
		assertEquals(1, jack.position());
		assertEquals(10, jill.position());
		assertEquals(jill, game.currentPlayer());
		return game;
	}

	@Given("move7jackBouncesBackToJill")
	public Game move8jillWins(Game game) {
		game.movePlayer(2);
		assertTrue(game.isOver());
		assertEquals(1, jack.position());
		assertEquals(12, jill.position());
		assertEquals(jack, game.currentPlayer());
		assertEquals(jill, game.winner());
		return game;
	}
}
