package layout;

import java.util.ArrayList;
import java.util.List;

public class CompositeMove implements Movable {
	private final List<Movable> moves = new ArrayList<>(); // List of all card moves.

	public CompositeMove(Movable... move) { // Add all move in the list.
		for (Movable m : move) {
			moves.add(m);
		}
	}

	@Override
	public void perform() { // Performs a move.
		for (Movable move : moves) {
			move.perform();
		}
	}
}
