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

public class FoundationView extends StackPane implements GameModelListenable { // Shows a completed stack of cards.
	private static final int PADDING = 5; // Foundation pile's padding.
	private static final String BORDER_STYLE = "-fx-border-color: lightgray;" + "-fx-border-width: 3;"
			+ " -fx-border-radius: 10.0"; // Foundation pile's normal border.
	private static final String BORDER_STYLE_DRAGGED = "-fx-border-color: darkgray;" + "-fx-border-width: 3;"
			+ " -fx-border-radius: 10.0"; // Foundation pile's dragged border.
	private DragHandler dh; // Drag handler for cards in the foundation pile.
	private Foundation index; // Index of foundation pile.

	public FoundationView(Foundation index) { // FoundationView's constructor.
		this.index = index;
		setPadding(new Insets(PADDING));
		setStyle(BORDER_STYLE);
		ImageView image = new ImageView(CardPictures.getBack());
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
	public void gameStateChanged() { // Check the game state of the foundation pile.
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

	public EventHandler<DragEvent> createOnDragOverHandler(ImageView image) { // Call when detected that dragged object
																				// is stay over the destination.
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent de) {
				if (de.getGestureSource() != image && de.getDragboard().hasString()) {
					TransferedStack transfer = new TransferedStack(de.getDragboard().getString());
					if (transfer.size() == 1 && GameModel.instance().isLegalMove(transfer.getTop(), index)) {
						de.acceptTransferModes(TransferMode.MOVE);
					}
				}
				de.consume();
			}
		};
	}

	public EventHandler<DragEvent> createOnDragEnteredHandler() { // Call when detected that dragged object is enter the
																	// destination.
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent de) {
				TransferedStack transfer = new TransferedStack(de.getDragboard().getString());
				if (transfer.size() == 1 && GameModel.instance().isLegalMove(transfer.getTop(), index)) {
					setStyle(BORDER_STYLE_DRAGGED);
				}
				de.consume();
			}
		};
	}

	public EventHandler<DragEvent> createOnDragExitedHandler() { // Call when detected that dragged object is exit the
																	// origin.
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent de) {
				setStyle(BORDER_STYLE);
				de.consume();
			}
		};
	}

	public EventHandler<DragEvent> createOnDragDroppedHandler() { // Call when detected that dragged object is dropped
																	// at the destination.
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent de) {
				Dragboard db = de.getDragboard();
				boolean success = false;
				if (db.hasString()) {
					TransferedStack transfer = new TransferedStack(de.getDragboard().getString());
					GameModel.instance().getCardMove(transfer.getTop(), index).perform();
					success = true;
				}
				de.setDropCompleted(success);
				de.consume();
			}
		};
	}
}