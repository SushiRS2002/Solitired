package gui;

import card.Suit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import layout.Foundation;
import layout.Table;

public class Main extends Application {
	private static final int WIDTH = 1250;
	private static final int HEIGHT = 750;
	private static final int MARGIN_OUTER = 10;
	private static final String TITLE = "Solitired";

	private DeckView deckView = new DeckView();
	private DiscardView discardView = new DiscardView();
	private SuitPile[] suitPile = new SuitPile[Suit.values().length];
	private PileView[] pileView = new PileView[Table.values().length];

	public Main() {
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(TITLE);
		GridPane root = new GridPane();
		root.setStyle("-fx-background-color: green;");
		root.setHgap(MARGIN_OUTER);
		root.setVgap(MARGIN_OUTER);
		root.setPadding(new Insets(MARGIN_OUTER));
		root.add(deckView, 0, 0);
		root.add(discardView, 1, 0);
		for (Foundation index : Foundation.values()) {
			suitPile[index.ordinal()] = new SuitPile(index);
			root.add(suitPile[index.ordinal()], 3 + index.ordinal(), 0);
		}
		for (Table index : Table.values()) {
			pileView[index.ordinal()] = new PileView(index);
			root.add(pileView[index.ordinal()], index.ordinal(), 1);
		}
		primaryStage.setResizable(true);
		primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
		primaryStage.show();
	}
}
