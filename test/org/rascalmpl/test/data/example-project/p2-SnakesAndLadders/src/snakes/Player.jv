package snakes;

public class Player {
	
	private String name;
	private ISquare square;

	private boolean invariant() {
		return name != null
			&& square != null;
	}

	public Player(String name) {
		this.name = name;
		// invariant holds only after joining a game
	}

	public void joinGame(Game game) {
		square = game.firstSquare();
		square.enter(this);	
		assert invariant();
	}

	public int position() {
		assert invariant();
		return square.position();
	}

	public void moveForward(int moves) {
		assert moves > 0;
		square.leave(this);
		square = square.moveAndLand(moves);
		square.enter(this);
	}
	
	public String toString() {
		return name;
	}

	public ISquare square() {
		return square;
	}

	public boolean wins() {
		return square.isLastSquare();
	}

}
