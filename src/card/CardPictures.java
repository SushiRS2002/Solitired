package card;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;

public class CardPictures {
	private static final String[] VALUE_SHORTENED = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k" };
	private static final String[] SUIT_SHORTENED = { "c", "d", "h", "s" };
	private static Map<String, Image> cardsMap = new HashMap<String, Image>();

	public CardPictures() {

	}

	public static Image getCard(Card card) {
		assert card != null;
		return getCard(getCode(card));
	}

	private static Image getCard(String code) {
		Image image = (Image) cardsMap.get(code);
		if (image == null) {
			image = new Image(CardPictures.class.getClassLoader().getResourceAsStream(code + ".png"));
			cardsMap.put(code, image);
		}
		return image;
	}

	public static Image getBack() {
		return getCard("back");
	}

	private static String getCode(Card pCard) {
		return VALUE_SHORTENED[pCard.getVALUE().ordinal()] + SUIT_SHORTENED[pCard.getSUIT().ordinal()];
	}
}
