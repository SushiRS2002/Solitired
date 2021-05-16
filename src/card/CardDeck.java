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

	public void push(Card pCard) {
		assert pCard != null;
		deck.push(pCard);
	}

	public Card draw() {
		assert !isEmpty();
		return deck.pop();
	}

	public boolean isEmpty() {
		return deck.isEmpty();
	}
}
