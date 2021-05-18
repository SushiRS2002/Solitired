package layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

import card.Card;
import card.CardDeck;
import card.CardStacks;
import card.Value;

public class TablePile {
	private final Map<Table, CardStacks> tableMap = new HashMap<>();
	private final Set<Card> visible = new HashSet<>();

	public TablePile() {
		for (Table index : Table.values()) {
			tableMap.put(index, new CardStacks());
		}
	}

	public void initialize(CardDeck deck) {
		assert deck != null;
		visible.clear();
		for (int i = 0; i < Table.values().length; i++) {
			tableMap.get(Table.values()[i]).clear();
			for (int j = 0; j < i + 1; j++) {
				Card card = deck.draw();
				tableMap.get(Table.values()[i]).push(card);
				if (j == i) {
					visible.add(card);
				}
			}
		}
	}

	public boolean canMoveTo(Card card, Table index) {
		assert card != null && index != null;
		CardStacks stacks = tableMap.get(index);
		if (stacks.isEmpty()) {
			return card.getVALUE() == Value.KING;
		} else {
			return card.getVALUE().ordinal() == stacks.peek().getVALUE().ordinal() - 1;
		}
	}

	public boolean isBottomKing(Card card) {
		assert card != null && contains(card);
		return card.getVALUE() == Value.KING && tableMap.get(getPile(card)).peek(0) == card;
	}

	public CardStacks getPile(Table index) {
		assert index != null;
		return new CardStacks(tableMap.get(index));
	}

	public Table getPile(Card card) {
		assert contains(card);
		for (Table pile : Table.values()) {
			if (contains(card, pile)) {
				return pile;
			}
		}
		assert false;
		return null;
	}

	public boolean revealsTop(Card card) {
		assert card != null && contains(card);
		Optional<Card> previous = getPreviousCard(card);
		if (!previous.isPresent()) {
			return false;
		}
		return visible.contains(card) && !visible.contains(previous.get());
	}

	public Optional<Card> getPreviousCard(Card card) {
		Optional<Card> previous = Optional.empty();
		for (Card c : tableMap.get(getPile(card))) {
			if (c == card) {
				return previous;
			}
			previous = Optional.of(c);
		}
		return Optional.empty();
	}

	public void moveWithin(Card card, Table origin, Table destination) {
		assert card != null && origin != null && destination != null;
		assert contains(card, origin);
		assert isVisible(card);
		Stack<Card> temporary = new Stack<>();
		Card c = tableMap.get(origin).pop();
		temporary.push(c);
		while (c != card) {
			c = tableMap.get(origin).pop();
			temporary.push(c);
		}
		while (!temporary.isEmpty()) {
			tableMap.get(destination).push(temporary.pop());
		}
	}

	public CardStacks getSequence(Card card, Table index) {
		assert card != null && index != null;
		CardStacks stack = tableMap.get(index);
		List<Card> result = new ArrayList<>();
		boolean hasSeen = false;
		for (Card c : stack) {
			if (c == card) {
				hasSeen = true;
			}
			if (hasSeen) {
				result.add(c);
			}
		}
		return new CardStacks(result);
	}

	public void showTop(Table index) {
		assert !tableMap.get(index).isEmpty();
		visible.add(tableMap.get(index).peek());
	}

	public void hideTop(Table index) {
		assert !tableMap.get(index).isEmpty();
		visible.remove(tableMap.get(index).peek());
	}

	public boolean contains(Card card, Table index) {
		assert card != null && index != null;
		for (Card c : tableMap.get(index)) {
			if (c == card) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Card card) {
		assert card != null;
		for (Table index : Table.values()) {
			if (contains(card, index)) {
				return true;
			}
		}
		return false;
	}

	public boolean isVisible(Card card) {
		assert contains(card);
		return visible.contains(card);
	}

	public boolean isLowestVisible(Card card) {
		assert card != null && contains(card);
		if (!isVisible(card)) {
			return false;
		} else {
			Optional<Card> previousCard = getPreviousCard(card);
			return !previousCard.isPresent() || !isVisible(previousCard.get());
		}
	}

	public void pop(Table index) {
		assert !tableMap.get(index).isEmpty();
		visible.remove(tableMap.get(index).pop());
	}

	public void push(Card card, Table index) {
		assert card != null && index != null;
		tableMap.get(index).push(card);
		visible.add(card);
	}
}
