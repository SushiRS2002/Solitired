package gui;

import card.Card;
import card.CardStacks;

public class Transfer { // The transfer of cards.
	private static final String SEPARATOR = ";";
	private Card[] cards;

	public Transfer(String str) { // Transfer's constructor.
		assert str != null && str.length() > 0;
		String[] tokens = str.split(SEPARATOR);
		cards = new Card[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			cards[i] = Card.get(tokens[i]);
		}
		assert cards.length > 0;
	}

	public static String serialize(CardStacks stacks) { // An array of card's ID.
		String result = "";
		for (Card card : stacks) {
			result += card.getIDString() + SEPARATOR;
		}
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	public Card getTop() { // Returns the top card in the transfer.
		return cards[0];
	}

	public int size() { // Returns the transfer's size.
		return cards.length;
	}
}
