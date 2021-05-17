package card;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CardStacks implements Iterable<Card> {
	private final List<Card> CARDSLIST;

	public CardStacks() {
		CARDSLIST = new ArrayList<>();
	}

	public CardStacks(Iterable<Card> cards) {
		this();
		for (Card card : cards) {
			CARDSLIST.add(card);
		}
	}

	public void push(Card card) {
		assert card != null && !CARDSLIST.contains(card);
		CARDSLIST.add(card);
	}

	public Card pop() {
		assert !isEmpty();
		return CARDSLIST.remove(CARDSLIST.size() - 1);
	}

	public Card peek() {
		assert !isEmpty();
		return CARDSLIST.get(CARDSLIST.size() - 1);
	}

	public Card peek(int index) {
		assert index >= 0 && index < size();
		return CARDSLIST.get(index);
	}

	public int size() {
		return CARDSLIST.size();
	}

	public void clear() {
		CARDSLIST.clear();
	}

	public boolean isEmpty() {
		return CARDSLIST.size() == 0;
	}

	public Iterator<Card> iterator() {
		return CARDSLIST.iterator();
	}
}
