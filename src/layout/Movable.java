package layout;

public interface Movable {
	void perform(); // Performs a move.

	default boolean isNull() { // Checks if that move advances the game. False by default.
		return false;
	}
}
