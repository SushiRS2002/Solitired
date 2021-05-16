package layout;

import card.Card;
import card.CardStacks;

public interface GameModelViewable {
	boolean isDiscardPileEmpty();

	boolean isDeckEmpty();

	boolean isFoundationPileEmpty(Foundation index);

	Card peekDiscardPile();

	CardStacks getTablePile(Table index);

	boolean isVisibleInTablePile(Card card);

	boolean isLowestVisibleInTablePile(Card card);

	boolean isBottomKing(Card card);

	boolean isLegalMove(Card card, Locatable destination);

	Movable getNullMove();

	Movable getDiscardMove();

	Movable getCardMove(Card card, Locatable destination);
}
