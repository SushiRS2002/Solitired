package gui;

import card.CardPictures;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import layout.GameModel;
import layout.GameModelListenable;

public class DeckView extends HBox implements GameModelListenable { // Allows to click deck and draw cards, Listens to
																	// game state changes.
	private static final String BUTTON_STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;";
	private static final String BUTTON_STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6;";
	private static final int IMAGE_NEW_LINE_WIDTH = 10;
	private static final int IMAGE_FONT_SIZE = 22;

	public DeckView() {
		final Button button = new Button();
		button.setGraphic(new ImageView(CardPictures.getBack()));
		button.setStyle(BUTTON_STYLE_NORMAL);
		button.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				((Button) me.getSource()).setStyle(BUTTON_STYLE_PRESSED);
			}
		});
		button.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				((Button) me.getSource()).setStyle(BUTTON_STYLE_NORMAL);
				if (GameModel.instance().isDeckEmpty()) {
					GameModel.instance().reset();
				} else {
					GameModel.instance().getDiscardMove().perform();
				}
			}
		});
		getChildren().add(button);
		GameModel.instance().addListener(this);
	}

	public Canvas createNewGameImage() { // Game's canvas.
		double width = CardPictures.getBack().getWidth();
		double height = CardPictures.getBack().getHeight();
		Canvas canvas = new Canvas(width, height);
		GraphicsContext context = canvas.getGraphicsContext2D();
		context.setStroke(Color.DARKGREEN);
		context.setLineWidth(IMAGE_NEW_LINE_WIDTH);
		context.strokeOval(width / 4, height / 2 - width / 4 + IMAGE_FONT_SIZE, width / 2, width / 2);
		context.setTextAlign(TextAlignment.CENTER);
		context.setTextBaseline(VPos.CENTER);
		context.setFill(Color.DARKKHAKI);
		context.setFont(Font.font(Font.getDefault().getName(), IMAGE_FONT_SIZE));
		if (GameModel.instance().isCompleted()) {
			context.fillText("You won!", Math.round(width / 2), IMAGE_FONT_SIZE);
		} else {
			context.fillText("Give up?", Math.round(width / 2), IMAGE_FONT_SIZE);
		}
		context.setTextAlign(TextAlignment.CENTER);
		return canvas;
	}

	@Override
	public void gameStateChanged() {
		if (GameModel.instance().isDeckEmpty()) {
			((Button) getChildren().get(0)).setGraphic(createNewGameImage());
		} else {
			((Button) getChildren().get(0)).setGraphic(new ImageView(CardPictures.getBack()));
		}
	}

	public void reset() {
		getChildren().get(0).setVisible(true);
	}
}