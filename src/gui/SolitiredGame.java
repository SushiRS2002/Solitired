package gui;

import card.Suit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import layout.Foundation;
import layout.Table;

public class SolitiredGame extends Application { // Launches the application.
	private static final int WIDTH = 1250; // Application's width.
	private static final int HEIGHT = 750; // Application's height.
	private static final int MARGIN = 10; // Application's margin.
	private static final String TITLE = "Solitired"; // Application's title.
	private DeckView deckView = new DeckView(); // Initializing the deck.
	private DiscardView discardView = new DiscardView(); // Initializing the discard pile.
	private FoundationView[] suitPile = new FoundationView[Suit.values().length]; // Initializing the foundation piles.
	private TableView[] pileView = new TableView[Table.values().length]; // Initializing the table piles.

	public SolitiredGame() { // Default constructor.

	}

	public static void main(String[] args) { // Launch the application when called.
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) { // Start the stage.
		primaryStage.setTitle(TITLE);
		GridPane root = new GridPane();
		root.setStyle("-fx-background-color: green;");
		root.setHgap(MARGIN);
		root.setVgap(MARGIN);
		root.setPadding(new Insets(MARGIN));
		root.add(deckView, 0, 0);
		root.add(discardView, 1, 0);
		for (Foundation index : Foundation.values()) {
			suitPile[index.ordinal()] = new FoundationView(index);
			root.add(suitPile[index.ordinal()], 3 + index.ordinal(), 0);
		}
		for (Table index : Table.values()) {
			pileView[index.ordinal()] = new TableView(index);
			root.add(pileView[index.ordinal()], index.ordinal(), 1);
		}
		primaryStage.setResizable(true);
		primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
		primaryStage.show();
	}
}
