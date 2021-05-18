package layout;

import card.Card;
import card.CardStacks;

public interface GameModelViewable {
	boolean isDiscardPileEmpty(); // Checks if the discard pile is empty.

	boolean isDeckEmpty(); // Checks if the deck is empty.

	boolean isFoundationPileEmpty(Foundation index); // Checks if the foundation pile is empty.

	Card peekDiscardPile(); // Returns the topmost card of the discard pile.

	CardStacks getTablePile(Table index); // Returns a table pile via its index.

	boolean isVisibleInTablePile(Card card); // Checks if the card is visible in that table pile.

	boolean isLowestVisibleInTablePile(Card card); // Checks if the card is visible and in the lowest position.

	boolean isBottomKing(Card card); // Checks if the lowest card is a king.

	boolean isLegalMove(Card card, Locatable destination); // Checks if moving that card is able to do it.

	Movable getNullMove(); // Returns null move.

	Movable getDiscardMove(); // Returns discard move.

	Movable getCardMove(Card card, Locatable destination); // Returns card move.
}
