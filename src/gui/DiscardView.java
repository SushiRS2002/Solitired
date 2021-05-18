package gui;

import card.Card;
import card.CardPictures;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import layout.GameModel;
import layout.GameModelListenable;

public class DiscardView extends HBox implements GameModelListenable {
	private static final int PADDING = 5;
	private DragHandler dh;

	public DiscardView() {
		setPadding(new Insets(PADDING));
		ImageView image = new ImageView(CardPictures.getBack());
		image.setVisible(false);
		getChildren().add(image);
		dh = new DragHandler(image);
		image.setOnDragDetected(dh);
		GameModel.instance().addListener(this);
	}

	@Override
	public void gameStateChanged() {
		if (GameModel.instance().isDiscardPileEmpty()) {
			getChildren().get(0).setVisible(false);
		} else {
			getChildren().get(0).setVisible(true);
			Card topCard = GameModel.instance().peekDiscardPile();
			ImageView image = (ImageView) getChildren().get(0);
			image.setImage(CardPictures.getCard(topCard));
			dh.setCard(topCard);
		}
	}
}