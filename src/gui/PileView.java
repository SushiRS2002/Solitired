package gui;

import card.Card;
import card.CardPictures;
import card.CardStacks;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import layout.GameModel;
import layout.GameModelListenable;
import layout.Table;

public class PileView extends StackPane implements GameModelListenable {
	private static final int PADDING = 5;
	private static final int Y_OFFSET = 25;
	private static final ClipboardContent CLIPBOARD_CONTENT = new ClipboardContent();
	private Table index;

	public PileView(Table index) {
		this.index = index;
		setPadding(new Insets(PADDING));
		setAlignment(Pos.TOP_CENTER);
		buildLayout();
		GameModel.instance().addListener(this);
	}

	public static Image getImage(Card card) {
		if (GameModel.instance().isVisibleInTablePile(card)) {
			return CardPictures.getCard(card);
		} else {
			return CardPictures.getBack();
		}
	}

	public void buildLayout() {
		getChildren().clear();
		int offset = 0;
		CardStacks stack = GameModel.instance().getTablePile(index);
		if (stack.isEmpty()) {
			ImageView image = new ImageView(CardPictures.getBack());
			image.setVisible(false);
			getChildren().add(image);
			return;
		}
		for (Card cardView : stack) {
			final ImageView image = new ImageView(getImage(cardView));
			image.setTranslateY(Y_OFFSET * offset);
			offset++;
			getChildren().add(image);

			setOnDragOver(createDragOverHandler(image, cardView));
			setOnDragEntered(createDragEnteredHandler(image, cardView));
			setOnDragExited(createDragExitedHandler(image, cardView));
			setOnDragDropped(createDragDroppedHandler(image, cardView));

			if (GameModel.instance().isVisibleInTablePile(cardView)) {
				image.setOnDragDetected(createDragDetectedHandler(image, cardView));
			}
		}
	}

	public EventHandler<MouseEvent> createDragDetectedHandler(ImageView imageView, Card card) {
		return new EventHandler<MouseEvent>() {
			public void handle(MouseEvent pMouseEvent) {
				Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
				CLIPBOARD_CONTENT.putString(Transfer.serialize(GameModel.instance().getSubStack(card, index)));
				db.setContent(CLIPBOARD_CONTENT);
				pMouseEvent.consume();
			}
		};
	}

	public EventHandler<DragEvent> createDragOverHandler(ImageView imageView, Card card) {
		return new EventHandler<DragEvent>() {
			public void handle(DragEvent pEvent) {
				if (pEvent.getGestureSource() != imageView && pEvent.getDragboard().hasString()) {
					Transfer transfer = new Transfer(pEvent.getDragboard().getString());
					if (GameModel.instance().isLegalMove(transfer.getTop(), index)) {
						pEvent.acceptTransferModes(TransferMode.MOVE);
					}
				}
				pEvent.consume();
			}
		};
	}

	public EventHandler<DragEvent> createDragEnteredHandler(ImageView imageView, Card card) {
		return new EventHandler<DragEvent>() {
			public void handle(DragEvent pEvent) {
				Transfer transfer = new Transfer(pEvent.getDragboard().getString());
				if (GameModel.instance().isLegalMove(transfer.getTop(), index)) {
					imageView.setEffect(new DropShadow());
				}
				pEvent.consume();
			}
		};
	}

	public EventHandler<DragEvent> createDragExitedHandler(ImageView imageView, Card card) {
		return new EventHandler<DragEvent>() {
			public void handle(DragEvent pEvent) {
				imageView.setEffect(null);
				pEvent.consume();
			}
		};
	}

	public EventHandler<DragEvent> createDragDroppedHandler(ImageView imageView, Card card) {
		return new EventHandler<DragEvent>() {
			public void handle(DragEvent pEvent) {
				Dragboard db = pEvent.getDragboard();
				boolean success = false;
				if (db.hasString()) {
					GameModel.instance().getCardMove(new Transfer(db.getString()).getTop(), index).perform();
					success = true;
				}
				pEvent.setDropCompleted(success);
				pEvent.consume();
			}
		};
	}

	public void gameStateChanged() {
		buildLayout();
	}
}
