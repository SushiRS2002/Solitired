package card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
	private CardStacks deck;

	public CardDeck() {
		shuffle();
	}

	public void shuffle() {
		List<Card> cards = new ArrayList<>();
		for (Suit suit : Suit.values()) {
			for (Value value : Value.values()) {
				cards.add(Card.get(value, suit));
			}
		}
		Collections.shuffle(cards);
		deck = new CardStacks(cards);
	}

	public void push(Card card) {
		assert card != null;
		deck.push(card);
	}

	public Card draw() {
		assert !isEmpty();
		return deck.pop();
	}

	public boolean isEmpty() {
		return deck.isEmpty();
	}
}
