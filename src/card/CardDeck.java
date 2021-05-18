package card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
	private CardStacks deck; // A card deck.

	public CardDeck() { // Shuffles the deck when initialize it.
		shuffle();
	}

	public void shuffle() { // Reinitializes the deck.
		List<Card> cards = new ArrayList<>();
		for (Suit suit : Suit.values()) {
			for (Value value : Value.values()) {
				cards.add(Card.get(value, suit));
			}
		}
		Collections.shuffle(cards);
		deck = new CardStacks(cards);
	}

	public void push(Card card) { // Pushes a card on top of the deck.
		assert card != null;
		deck.push(card);
	}

	public Card draw() { // Draws and removes a card from the deck.
		assert !isEmpty();
		return deck.pop();
	}

	public boolean isEmpty() { // Checks if the deck is empty.
		return deck.isEmpty();
	}
}
