package layout;

public interface Movable {
	void perform();

	default boolean isNull() {
		return false;
	}
}
