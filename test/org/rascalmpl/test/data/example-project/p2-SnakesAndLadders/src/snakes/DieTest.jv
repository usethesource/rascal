package snakes;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DieTest {
	private static final int MAX = 20;
	
	@Test
	public void testInRange() {
		Die die = new Die();
		for (int i = 1;i<=MAX;i++) {
			int result = die.roll();
			assertTrue(result >= 1 && result <= Die.FACES);
		}
	}

	@Test
	public void testMinReached() {
		assertTrue(reached(1));
	}

	@Test
	public void testMaxReached() {
		assertTrue(reached(Die.FACES));
	}
	
	private boolean reached(int value) {
		Die die = new Die();
		for (int i = 1; i<=MAX; i++) {
			if (die.roll() == value) {
				return true;
			}
		}
		return false;
	}
}
