package layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import card.Card;
import card.CardDeck;
import card.CardStacks;
import card.Suit;
import card.Value;

public final class GameModel implements GameModelViewable {
	private static final GameModel INSTANCE = new GameModel(); // A model of game.
	private CardDeck deck = new CardDeck(); // A deck.
	private Stack<Movable> moves = new Stack<>(); // A stack of moves.
	private CardStacks discard = new CardStacks(); // A discard pile.
	private FoundationPile foundations = new FoundationPile(); // Four foundation piles.
	private TablePile tables = new TablePile(); // Seven table piles.
	private List<GameModelListenable> listeners = new ArrayList<>(); // A list of game state listeners.

	private static Movable nullMove = new Movable() { // A null move.
		@Override
		public void perform() {

		}

		@Override
		public boolean isNull() {
			return true;
		}
	};

	private Movable discardMove = new Movable() { // A discard move.
		@Override
		public void perform() {
			assert !isDeckEmpty();
			discard.push(deck.draw());
			moves.push(this);
			notifyListeners();
		}
	};

	public GameModel() { // Reset when called.
		reset();
	}

	public int getScore() { // Return the number of cards in foundation piles.
		return foundations.getTotalSize();
	}

	public static GameModel instance() { // A singleton instance.
		return INSTANCE;
	}

	public void addListener(GameModelListenable listener) { // Add a listener in the list.
		assert listener != null;
		listeners.add(listener);
	}

	public void notifyListeners() { // Called when game state has been changed.
		for (GameModelListenable listener : listeners) {
			listener.gameStateChanged();
		}
	}

	public void reset() { // Reset the game.
		moves.clear();
		deck.shuffle();
		discard.clear();
		foundations.initialize();
		tables.initialize(deck);
		notifyListeners();
	}

	public boolean isCompleted() { // Checks if the game is completed.
		return foundations.getTotalSize() == Value.values().length * Suit.values().length;
	}

	@Override
	public boolean isDeckEmpty() { // Checks if the deck is empty.
		return deck.isEmpty();
	}

	@Override
	public boolean isDiscardPileEmpty() { // Checks if the discard pile is empty.
		return discard.isEmpty();
	}

	@Override
	public boolean isFoundationPileEmpty(Foundation index) { // Checks if the foundation pile is empty.
		return foundations.isEmpty(index);
	}

	public Card peekSuitStack(Foundation index) { // Returns the topmost card of that foundation pile.
		assert index != null && !isFoundationPileEmpty(index);
		return foundations.peek(index);
	}

	@Override
	public Card peekDiscardPile() { // Returns the topmost card of the discard pile.
		assert discard.size() != 0;
		return discard.peek();
	}

	public Locatable find(Card card) { // Returns the card's location.
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

	public void absorbCard(Locatable location) { // Removes a card from that location.
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

	public void move(Card card, Locatable destination) { // Move a card from A to B.
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
	public CardStacks getTablePile(Table index) { // Returns a table pile via its index.
		return tables.getPile(index);
	}

	@Override
	public boolean isVisibleInTablePile(Card card) { // Checks if the card is visible in that table pile.
		return tables.contains(card) && tables.isVisible(card);
	}

	@Override
	public boolean isLowestVisibleInTablePile(Card card) { // Checks if the card is visible and in the lowest position.
		return tables.contains(card) && tables.isLowestVisible(card);
	}

	public CardStacks getSubStack(Card card, Table index) { // Get the stack of the card and the ones below it.
		assert card != null && index != null && find(card) == index;
		return tables.getSequence(card, index);
	}

	@Override
	public boolean isLegalMove(Card card, Locatable destination) { // Checks if moving that card is able to do it.
		if (destination instanceof Foundation) {
			return foundations.canMoveTo(card, (Foundation) destination);
		} else if (destination instanceof Table) {
			return tables.canMoveTo(card, (Table) destination);
		} else {
			return false;
		}
	}

	@Override
	public Movable getNullMove() { // Returns null move.
		return nullMove;
	}

	@Override
	public Movable getDiscardMove() { // Returns discard move.
		return discardMove;
	}

	@Override
	public Movable getCardMove(Card card, Locatable destination) { // Returns card move.
		Locatable source = find(card);
		if (source instanceof Table && tables.revealsTop(card)) {
			return new CompositeMove(new CardMove(card, destination), new RevealTopMove((Table) source));
		}
		return new CardMove(card, destination);
	}

	@Override
	public boolean isBottomKing(Card card) { // Checks if the lowest card is a king.
		assert card != null && tables.contains(card);
		return tables.isBottomKing(card);
	}

	public class CardMove implements Movable { // A move of card stacks.
		private Card card;
		private Locatable origin;
		private Locatable destination;

		public CardMove(Card card, Locatable destination) {
			this.card = card;
			this.destination = destination;
			origin = find(card);
		}

		@Override
		public void perform() {
			assert isLegalMove(card, destination);
			move(card, destination);
			moves.push(this);
		}

	}

	public class RevealTopMove implements Movable { // Reveals the top of the stack.
		private final Table index;

		public RevealTopMove(Table index) {
			this.index = index;
		}

		@Override
		public void perform() {
			tables.showTop(index);
			moves.push(this);
			notifyListeners();
		}

	}
}
