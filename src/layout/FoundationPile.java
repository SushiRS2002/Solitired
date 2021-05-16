package layout;

import java.util.HashMap;
import java.util.Map;

import card.Card;
import card.CardStacks;
import card.Value;

public class FoundationPile {
	private Map<Foundation, CardStacks> piles = new HashMap<>();

	public FoundationPile() {
		initialize();
	}

	public int getTotalSize() {
		int summary = 0;
		for (CardStacks stack : piles.values()) {
			summary += stack.size();
		}
		return summary;
	}

	public void initialize() {
		for (Foundation index : Foundation.values()) {
			piles.put(index, new CardStacks());
		}
	}

	public boolean isEmpty(Foundation index) {
		assert index != null;
		return piles.get(index).isEmpty();
	}

	public boolean canMoveTo(Card card, Foundation index) {
		assert card != null && index != null;
		if (isEmpty(index)) {
			return card.getVALUE() == Value.ACE;
		} else {
			return (card.getVALUE().ordinal() == peek(index).getVALUE().ordinal() + 1)
					&& (card.getSUIT() == peek(index).getSUIT());
		}
	}

	public Card peek(Foundation index) {
		assert index != null && !piles.get(index).isEmpty();
		return piles.get(index).peek();
	}

	public Card pop(Foundation index) {
		assert index != null && !isEmpty(index);
		return piles.get(index).pop();
	}

	public void push(Card card, Foundation index) {
		assert card != null && index != null;
		piles.get(index).push(card);
	}
}
