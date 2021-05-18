package card;

public final class Card {
	private static final Card[][] CARDS = new Card[Suit.values().length][]; // A 2D list which stores 52 card.
	private final Value VALUE; // Card's value. Range from 1 (Ace) to 13 (King).
	private final Suit SUIT; // Card's suit (Club, Diamond, Heart, Spade).
	static { // Initializing 2D list by adding all 52 cards.
		for (Suit suit : Suit.values()) {
			CARDS[suit.ordinal()] = new Card[Value.values().length];
			for (Value value : Value.values()) {
				CARDS[suit.ordinal()][value.ordinal()] = new Card(value, suit);
			}
		}
	}

	public Card(Value value, Suit suit) { // Card's constructor.
		VALUE = value;
		SUIT = suit;
	}

	public static Card get(Value value, Suit suit) { // Returns a card via (value, suit) from 2D list.
		assert value != null && suit != null;
		return CARDS[suit.ordinal()][value.ordinal()];
	}

	public static Card get(String str) { // Returns a card via its ID from 2D list.
		assert str != null;
		int id = Integer.parseInt(str);
		return get(Value.values()[id % Value.values().length], Suit.values()[id / Value.values().length]);
	}

	public String getIDString() { // Returns a card's ID.
		return Integer.toString(getSUIT().ordinal() * Value.values().length + getVALUE().ordinal());
	}

	public Value getVALUE() { // Returns a card's value.
		return VALUE;
	}

	public Suit getSUIT() { // Returns a card's suit.
		return SUIT;
	}

	@Override
	public String toString() { // toString() of each card.
		return VALUE + " of " + SUIT;
	}
}
