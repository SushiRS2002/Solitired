package gui;

import card.Card;
import card.CardPictures;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import layout.Foundation;
import layout.GameModel;
import layout.GameModelListenable;

public class SuitPile extends StackPane implements GameModelListenable {
	private static final int PADDING = 5;
	private static final String BORDER_STYLE = "-fx-border-color: lightgray;" + "-fx-border-width: 3;"
			+ " -fx-border-radius: 10.0";
	private static final String BORDER_STYLE_DRAGGED = "-fx-border-color: darkgray;" + "-fx-border-width: 3;"
			+ " -fx-border-radius: 10.0";
	private static final String BORDER_STYLE_NORMAL = "-fx-border-color: lightgray;" + "-fx-border-width: 3;"
			+ " -fx-border-radius: 10.0";
	private DragHandler dh;
	private Foundation index;

	public SuitPile(Foundation index) {
		this.index = index;
		setPadding(new Insets(PADDING));
		setStyle(BORDER_STYLE);
		final ImageView image = new ImageView(CardPictures.getBack());
		image.setVisible(false);
		getChildren().add(image);
		dh = new DragHandler(image);
		image.setOnDragDetected(dh);
		setOnDragOver(createOnDragOverHandler(image));
		setOnDragEntered(createOnDragEnteredHandler());
		setOnDragExited(createOnDragExitedHandler());
		setOnDragDropped(createOnDragDroppedHandler());
		GameModel.instance().addListener(this);
	}

	@Override
	public void gameStateChanged() {
		if (GameModel.instance().isFoundationPileEmpty(index)) {
			getChildren().get(0).setVisible(false);
		} else {
			getChildren().get(0).setVisible(true);
			Card topCard = GameModel.instance().peekSuitStack(index);
			ImageView image = (ImageView) getChildren().get(0);
			image.setImage(CardPictures.getCard(topCard));
			dh.setCard(topCard);
		}
	}

	private EventHandler<DragEvent> createOnDragOverHandler(ImageView view) {
		return new EventHandler<DragEvent>() {
			public void handle(DragEvent de) {
				if (de.getGestureSource() != view && de.getDragboard().hasString()) {
					Transfer transfer = new Transfer(de.getDragboard().getString());
					if (transfer.size() == 1 && GameModel.instance().isLegalMove(transfer.getTop(), index)) {
						de.acceptTransferModes(TransferMode.MOVE);
					}
				}
				de.consume();
			}
		};
	}

	private EventHandler<DragEvent> createOnDragEnteredHandler() {
		return new EventHandler<DragEvent>() {
			public void handle(DragEvent de) {
				Transfer transfer = new Transfer(de.getDragboard().getString());
				if (transfer.size() == 1 && GameModel.instance().isLegalMove(transfer.getTop(), index)) {
					setStyle(BORDER_STYLE_DRAGGED);
				}
				de.consume();
			}
		};
	}

	private EventHandler<DragEvent> createOnDragExitedHandler() {
		return new EventHandler<DragEvent>() {
			public void handle(DragEvent pEvent) {
				setStyle(BORDER_STYLE_NORMAL);
				pEvent.consume();
			}
		};
	}

	private EventHandler<DragEvent> createOnDragDroppedHandler() {
		return new EventHandler<DragEvent>() {
			public void handle(DragEvent pEvent) {
				Dragboard db = pEvent.getDragboard();
				boolean success = false;
				if (db.hasString()) {
					Transfer transfer = new Transfer(pEvent.getDragboard().getString());
					GameModel.instance().getCardMove(transfer.getTop(), index).perform();
					success = true;
				}
				pEvent.setDropCompleted(success);
				pEvent.consume();
			}
		};
	}
}
