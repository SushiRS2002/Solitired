package card;

public class Card {
	private static final Card[][] CARDS = new Card[Suit.values().length][];
	private final Suit SUIT;
	private final Value VALUE;

	static {
		for (Suit suit : Suit.values()) {
			CARDS[suit.ordinal()] = new Card[Value.values().length];
			for (Value value : Value.values()) {
				CARDS[suit.ordinal()][value.ordinal()] = new Card(value, suit);
			}
		}
	}

	public Card(Value value, Suit suit) {
		this.VALUE = value;
		this.SUIT = suit;
	}

	public static Card get(Value value, Suit suit) {
		assert value != null && suit != null;
		return CARDS[suit.ordinal()][value.ordinal()];
	}

	public static Card get(String str) {
		assert str != null;
		int id = Integer.parseInt(str);
		return get(Value.values()[id % Value.values().length], Suit.values()[id / Value.values().length]);
	}

	public String getIDString() {
		return Integer.toString(getSUIT().ordinal() * Value.values().length + getVALUE().ordinal());
	}

	public Suit getSUIT() {
		return SUIT;
	}

	public Value getVALUE() {
		return VALUE;
	}

	public String toString() {
		return VALUE + " of " + SUIT;
	}
}
