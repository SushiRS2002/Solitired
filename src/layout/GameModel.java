package layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import card.Card;
import card.CardDeck;
import card.CardStacks;
import card.Value;
import card.Suit;

public class GameModel implements GameModelViewable {
	private static GameModel instance = new GameModel();
	private final CardDeck deck = new CardDeck();
	private final Stack<Movable> moves = new Stack<>();
	private final CardStacks discard = new CardStacks();
	private final FoundationPile foundations = new FoundationPile();
	private final TablePile tables = new TablePile();
	private final List<GameModelListenable> listeners = new ArrayList<>();

	private static Movable nullMove = new Movable() {
		public void perform() {

		}

		public boolean isNull() {
			return true;
		}
	};

	private final Movable discardMove = new Movable() {
		public void perform() {
			assert !isDeckEmpty();
			discard.push(deck.draw());
			moves.push(this);
			notifyListeners();
		}
	};

	public GameModel() {
		reset();
	}

	public int getScore() {
		return foundations.getTotalSize();
	}

	public static GameModel instance() {
		return instance;
	}

	public void addListener(GameModelListenable listener) {
		assert listener != null;
		listeners.add(listener);
	}

	public void notifyListeners() {
		for (GameModelListenable listener : listeners) {
			listener.gameStateChanged();
		}
	}

	public void reset() {
		moves.clear();
		deck.shuffle();
		discard.clear();
		foundations.initialize();
		tables.initialize(deck);
		notifyListeners();
	}

	public boolean isCompleted() {
		return foundations.getTotalSize() == Value.values().length * Suit.values().length;
	}

	public boolean isDeckEmpty() {
		return deck.isEmpty();
	}

	public boolean isDiscardPileEmpty() {
		return discard.isEmpty();
	}

	public boolean isFoundationPileEmpty(Foundation pile) {
		return foundations.isEmpty(pile);
	}

	public Card peekSuitStack(Foundation pile) {
		assert pile != null && !isFoundationPileEmpty(pile);
		return foundations.peek(pile);
	}

	public Card peekDiscardPile() {
		assert discard.size() != 0;
		return discard.peek();
	}

	private Locatable find(Card card) {
		if (!discard.isEmpty() && discard.peek() == card) {
			return Discard.DISCARD_PILE;
		}
		for (Foundation index : Foundation.values()) {
			if (!foundations.isEmpty(index) && foundations.peek(index) == card) {
				return index;
			}
		}
		for (Table index : Table.values()) {
			if (tables.contains(card, index)) {
				return index;
			}
		}
		assert false;
		return null;
	}

	private void absorbCard(Locatable location) {
		if (location == Discard.DISCARD_PILE) {
			assert !discard.isEmpty();
			discard.pop();
		} else if (location instanceof Foundation) {
			assert !foundations.isEmpty((Foundation) location);
			foundations.pop((Foundation) location);
		} else {
			assert location instanceof Table;
			tables.pop((Table) location);
		}
	}

	private void move(Card card, Locatable destination) {
		Locatable source = find(card);
		if (source instanceof Table && destination instanceof Table) {
			tables.moveWithin(card, (Table) source, (Table) destination);
		} else {
			absorbCard(source);
			if (destination instanceof Foundation) {
				foundations.push(card, (Foundation) destination);
			} else if (destination == Discard.DISCARD_PILE) {
				discard.push(card);
			} else {
				assert destination instanceof Table;
				tables.push(card, (Table) destination);
			}
		}
		notifyListeners();
	}

	@Override
	public CardStacks getTablePile(Table index) {
		return tables.getPile(index);
	}

	@Override
	public boolean isVisibleInTablePile(Card card) {
		return tables.contains(card) && tables.isVisible(card);
	}

	public boolean isLowestVisibleInTablePile(Card card) {
		return tables.contains(card) && tables.isLowestVisible(card);
	}

	public CardStacks getSubStack(Card card, Table pile) {
		assert card != null && pile != null && find(card) == pile;
		return tables.getSequence(card, pile);
	}

	public boolean isLegalMove(Card card, Locatable destination) {
		if (destination instanceof FoundationPile) {
			return foundations.canMoveTo(card, (Foundation) destination);
		} else if (destination instanceof Table) {
			return tables.canMoveTo(card, (Table) destination);
		} else {
			return false;
		}
	}

	public Movable getNullMove() {
		return nullMove;
	}

	public Movable getDiscardMove() {
		return discardMove;
	}

	public Movable getCardMove(Card card, Locatable destination) {
		Locatable source = find(card);
		if (source instanceof Table && tables.revealsTop(card)) {
			return new CompositeMove(new CardMove(card, destination), new RevealTopMove((Table) source));
		}
		return new CardMove(card, destination);
	}

	public boolean isBottomKing(Card card) {
		assert card != null && tables.contains(card);
		return tables.isBottomKing(card);
	}

	private class CardMove implements Movable {
		private Card card;
		private Locatable origin;
		private Locatable destination;

		public CardMove(Card c, Locatable d) {
			card = c;
			destination = d;
			origin = find(c);
		}

		public void perform() {
			assert isLegalMove(card, destination);
			move(card, destination);
			moves.push(this);
		}

	}

	private class RevealTopMove implements Movable {
		private final Table index;

		public RevealTopMove(Table i) {
			index = i;
		}

		public void perform() {
			tables.showTop(index);
			moves.push(this);
			notifyListeners();
		}
	}
}
