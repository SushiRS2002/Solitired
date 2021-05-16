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
	private Map<Table, CardStacks> tableMap = new HashMap<>();
	private Set<Card> visible = new HashSet<>();

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

	public boolean canMoveTo(Card card, Table table) {
		assert card != null && table != null;
		CardStacks pile = tableMap.get(table);
		if (pile.isEmpty()) {
			return card.getVALUE() == Value.KING;
		} else {
			return (card.getVALUE().ordinal() == pile.peek().getVALUE().ordinal() - 1);
		}
	}

	public boolean isBottomKing(Card card) {
		assert card != null && contains(card);
		return (card.getVALUE() == Value.KING) && (tableMap.get(getPile(card)).peek(0) == card);
	}

	public CardStacks getPile(Table table) {
		assert table != null;
		return new CardStacks(tableMap.get(table));
	}

	public Table getPile(Card card) {
		assert contains(card);
		for (Table table : Table.values()) {
			if (contains(card, table)) {
				return table;
			}
		}
		assert false;
		return null;
	}

	public boolean revealsTop(Card card) {
		assert card != null && contains(card);
		Optional<Card> before = getPreviousCard(card);
		if (!before.isPresent()) {
			return false;
		}
		return visible.contains(card) && !visible.contains(before.get());
	}

	public Optional<Card> getPreviousCard(Card card) {
		Optional<Card> before = Optional.empty();
		for (Card c : tableMap.get(getPile(card))) {
			if (c == card) {
				return before;
			}
			before = Optional.of(card);
		}
		return Optional.empty();
	}

	public void moveWithin(Card card, Table originTable, Table finalTable) {
		assert card != null && originTable != null && finalTable != null;
		assert contains(card, originTable);
		assert isVisible(card);
		Stack<Card> temporary = new Stack<>();
		Card c = tableMap.get(originTable).pop();
		temporary.push(c);
		while (c != card) {
			c = tableMap.get(originTable).pop();
			temporary.push(c);
		}
		while (!temporary.isEmpty()) {
			tableMap.get(finalTable).push(temporary.pop());
		}
	}

	public CardStacks getSequence(Card card, Table table) {
		assert card != null && table != null;
		CardStacks stack = tableMap.get(table);
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
			Optional<Card> olderCard = getPreviousCard(card);
			return !olderCard.isPresent() || !isVisible(olderCard.get());
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
