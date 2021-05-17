package card;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public final class CardPictures {
	private static final String[] VALUE_CODES = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k" };
	private static final String[] SUIT_CODES = { "c", "d", "h", "s" };
	private static Map<String, Image> cardsMap = new HashMap<String, Image>();
	
	public CardPictures() {

	}

	public static Image getCard(Card card) {
		assert card != null;
		return getCard(getCode(card));
	}

	public static Image getBack() {
		return getCard("back");
	}

	public static Image getCard(String code) {
		Image image = (Image) cardsMap.get(code);
		if (image == null) {
			image = new Image(CardPictures.class.getClassLoader().getResourceAsStream(code + ".png"));
			cardsMap.put(code, image);
		}
		return image;
	}

	public static String getCode(Card card) {
		return VALUE_CODES[card.getVALUE().ordinal()] + SUIT_CODES[card.getSUIT().ordinal()];
	}
}
