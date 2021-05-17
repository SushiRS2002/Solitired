package layout;

import java.util.HashMap;
import java.util.Map;

import card.Card;
import card.CardStacks;
import card.Value;

public class FoundationPile {
	private final Map<Foundation, CardStacks> foundMap = new HashMap<>();

	public FoundationPile() {
		initialize();
	}

	public int getTotalSize() {
		int sum = 0;
		for (CardStacks stack : foundMap.values()) {
			sum += stack.size();
		}
		return sum;
	}

	public void initialize() {
		for (Foundation index : Foundation.values()) {
			foundMap.put(index, new CardStacks());
		}
	}

	public boolean isEmpty(Foundation location) {
		assert location != null;
		return foundMap.get(location).isEmpty();
	}

	public boolean canMoveTo(Card card, Foundation location) {
		assert card != null && location != null;
		if (isEmpty(location)) {
			return card.getVALUE() == Value.ACE;
		} else {
			return card.getSUIT() == peek(location).getSUIT()
					&& card.getVALUE().ordinal() == peek(location).getVALUE().ordinal() + 1;
		}
	}

	public Card peek(Foundation location) {
		assert location != null && !foundMap.get(location).isEmpty();
		return foundMap.get(location).peek();
	}

	public void push(Card card, Foundation location) {
		assert card != null && location != null;
		foundMap.get(location).push(card);
	}

	public Card pop(Foundation location) {
		assert location != null && !isEmpty(location);
		return foundMap.get(location).pop();
	}
}
