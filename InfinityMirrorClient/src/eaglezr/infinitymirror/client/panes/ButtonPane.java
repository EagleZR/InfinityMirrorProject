package eaglezr.infinitymirror.client.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ButtonPane extends GridPane {
	private static final int MIN_WIDTH = 2;
	private static final int PREF_MAX_HEIGHT = 3;

	public ButtonPane(Button... buttons) {
		int width = (buttons.length <= MIN_WIDTH * PREF_MAX_HEIGHT ? MIN_WIDTH
				: (buttons.length / PREF_MAX_HEIGHT + (buttons.length % PREF_MAX_HEIGHT == 0 ? 0 : 1)));

		int height = (buttons.length / width > 0 ? buttons.length / width + (buttons.length % width == 0 ? 0 : 1)
				: 1);

		int u = 0;
		for (int x = 0; x < width && u < buttons.length; x++) {
			for (int y = 0; y < height && u < buttons.length; y++, u++) {
				// FIXME Decide which option to use
				// Option 1
				// BorderPane pane = new BorderPane();
				// pane.setCenter(buttons[u]);
				// super.add(pane, x, y);
				// pane.prefHeightProperty().bind(this.heightProperty().divide(height));
				// pane.prefWidthProperty().bind(this.widthProperty().divide(width));

				// Option 2
				super.add(buttons[u], x, y);
				buttons[u].prefHeightProperty().bind(this.heightProperty().divide(height));
				buttons[u].prefWidthProperty().bind(this.widthProperty().divide(width));
			}
		}
	}
}