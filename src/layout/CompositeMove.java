package layout;

import java.util.ArrayList;
import java.util.List;

public class CompositeMove implements Movable {
	private final List<Movable> moves = new ArrayList<>();

	public CompositeMove(Movable... move) {
		for (Movable m : move) {
			moves.add(m);
		}
	}

	@Override
	public void perform() {
		for (Movable move : moves) {
			move.perform();
		}
	}
}
