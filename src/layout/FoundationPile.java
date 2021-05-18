package layout;

import java.util.HashMap;
import java.util.Map;

import card.Card;
import card.CardStacks;
import card.Value;

public class FoundationPile {
	private final Map<Foundation, CardStacks> foundMap = new HashMap<>(); // A map of foundations.

	public FoundationPile() { // Initializing foundation piles when called.
		initialize();
	}

	public int getTotalSize() { // Returns the total number of cards in foundation piles.
		int sum = 0;
		for (CardStacks stack : foundMap.values()) {
			sum += stack.size();
		}
		return sum;
	}

	public void initialize() { // Initializing four foundation piles.
		for (Foundation index : Foundation.values()) {
			foundMap.put(index, new CardStacks());
		}
	}

	public boolean isEmpty(Foundation location) { // Checks if that foundation pile is empty.
		assert location != null;
		return foundMap.get(location).isEmpty();
	}

	public boolean canMoveTo(Card card, Foundation location) { // Checks if we can move a card to that foundation pile.
		assert card != null && location != null;
		if (isEmpty(location)) {
			return card.getVALUE() == Value.ACE;
		} else {
			return card.getSUIT() == peek(location).getSUIT()
					&& card.getVALUE().ordinal() == peek(location).getVALUE().ordinal() + 1;
		}
	}

	public Card peek(Foundation location) { // Returns a topmost card of that foundation pile.
		assert location != null && !foundMap.get(location).isEmpty();
		return foundMap.get(location).peek();
	}

	public void push(Card card, Foundation location) { // Places a card on the foundation pile.
		assert card != null && location != null;
		foundMap.get(location).push(card);
	}

	public Card pop(Foundation location) { // Removes and returns a topmost card of that foundation pile.
		assert location != null && !isEmpty(location);
		return foundMap.get(location).pop();
	}
}
