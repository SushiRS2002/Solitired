package card;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CardStacks implements Iterable<Card> {
	private final List<Card> CARDSLIST; // List of cards.

	public CardStacks() { // CardStacks's constructor.
		CARDSLIST = new ArrayList<>();
	}

	public CardStacks(Iterable<Card> cards) { // CardStacks which contains all card in cards.
		this();
		for (Card card : cards) {
			CARDSLIST.add(card);
		}
	}

	public void push(Card card) { // Pushes card into the stack.
		assert card != null && !CARDSLIST.contains(card);
		CARDSLIST.add(card);
	}

	public Card pop() { // Removes and returns the topmost card on the stack.
		assert !isEmpty();
		return CARDSLIST.remove(CARDSLIST.size() - 1);
	}

	public Card peek() { // Returns the topmost card on the stack.
		assert !isEmpty();
		return CARDSLIST.get(CARDSLIST.size() - 1);
	}

	public Card peek(int index) { // Returns the card on the stack of that index.
		assert index >= 0 && index < size();
		return CARDSLIST.get(index);
	}

	public int size() { // Returns stack's size.
		return CARDSLIST.size();
	}

	public void clear() { // Removes all card in the stack.
		CARDSLIST.clear();
	}

	public boolean isEmpty() { // Checks if the stack is empty.
		return CARDSLIST.size() == 0;
	}

	@Override
	public Iterator<Card> iterator() {
		return CARDSLIST.iterator();
	}
}
