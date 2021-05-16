package layout;

import java.util.ArrayList;
import java.util.List;

public class CompositeMove implements Movable {
	private List<Movable> moves = new ArrayList<>();

	public void perform() {
		for (Movable move : moves) {
			move.perform();
		}
	}

	public CompositeMove(Movable... m) {
		for (Movable move : m) {
			moves.add(move);
		}
	}
}
