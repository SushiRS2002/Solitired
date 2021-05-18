package gui;

import card.Card;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class DragHandler implements EventHandler<MouseEvent> { // Stores a string represents the dragged card.
	private static final ClipboardContent CLIPBOARD_CONTENT = new ClipboardContent();
	private Card card; // A card
	private ImageView image; // A card's image

	public DragHandler(ImageView image) { // DragHandler's constructor.
		this.image = image;
	}

	public void setCard(Card card) { // Set a card.
		this.card = card;
	}

	@Override
	public void handle(MouseEvent me) {
		Dragboard db = image.startDragAndDrop(TransferMode.ANY);
		CLIPBOARD_CONTENT.putString(card.getIDString());
		db.setContent(CLIPBOARD_CONTENT);
		me.consume();
	}
}