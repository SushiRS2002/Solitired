package card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
	private CardStacks stack;

	public CardDeck() {
		shuffle();
	}

	public void shuffle() {
		List<Card> cards = new ArrayList<>();
		for (Suit suit : Suit.values()) {
			for (Value rank : Value.values()) {
				cards.add(Card.get(rank, suit));
			}
		}
		Collections.shuffle(cards);
		stack = new CardStacks(cards);
	}

	public void push(Card card) {
		assert card != null;
		stack.push(card);
	}

	public Card draw() {
		assert !isEmpty();
		return stack.pop();
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}
}
