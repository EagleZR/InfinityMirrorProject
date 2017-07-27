package eaglezr.javafx.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ButtonPane extends GridPane {

	int minWidth = 2;
	int prefMaxHeight = 3;

	Button[] buttons;

	public ButtonPane( Button... buttons ) {
		setButtons( buttons );
	}

	public ButtonPane( int minWidth, int prefMaxHeight, Button... buttons ) {
		this.minWidth = minWidth;
		this.prefMaxHeight = prefMaxHeight;
		setButtons( buttons );
	}

	public void setButtons( Button... buttons ) {
		this.buttons = buttons;
		int width = ( buttons.length <= minWidth * prefMaxHeight ?
				minWidth :
				( buttons.length / prefMaxHeight + ( buttons.length % prefMaxHeight == 0 ? 0 : 1 ) ) );

		int height = ( buttons.length / width > 0 ?
				buttons.length / width + ( buttons.length % width == 0 ? 0 : 1 ) :
				1 );

		int u = 0;
		for ( int x = 0; x < width && u < buttons.length; x++ ) {
			for ( int y = 0; y < height && u < buttons.length; y++, u++ ) {
				super.add( buttons[u], x, y );
				buttons[u].prefHeightProperty().bind( this.heightProperty().divide( height ) );
				buttons[u].prefWidthProperty().bind( this.widthProperty().divide( width ) );
			}
		}
	}

	public void addButton( Button button ) {
		addButtons( button );
	}

	public void addButtons( Button... buttons ) {
		Button[] newButtons = new Button[this.buttons.length + buttons.length];
		int i = 0;
		boolean nullFlag = false;
		for ( ; i < this.buttons.length; i++ ) {
			newButtons[i] = this.buttons[i];
		}
		for ( int u = 0; u < buttons.length; u++ ) {
			if ( buttons[i] != null ) {
				newButtons[i + u] = buttons[u];
			} else {
				nullFlag = true;
			}
		}
		if ( !nullFlag ) {
			setButtons( newButtons );
		} else {
			removeButtons( null );
		}
	}

	public void removeButton( Button button ) {
		removeButtons( button );
	}

	public void removeButtons( Button... buttons ) {
		int count = 0; // Counts how many are actually removed.
		for ( int i = 0; i < this.buttons.length; i++ ) {
			for ( int u = 0; u < buttons.length; u++ ) {
				if ( this.buttons[i].equals( buttons[u] ) ) {
					this.buttons[i] = null;
					count++;
				}
			}
		}
		Button[] newButtons = new Button[this.buttons.length - count];
		for ( int i = 0, u = 0; i < this.buttons.length; i++ ) {
			if ( this.buttons[i] != null ) {
				newButtons[u] = this.buttons[i];
				u++;
			}
		}
		setButtons( newButtons );
	}

	public Button[] getButtons() {
		return this.buttons;
	}

	public int getPrefMaxHeight() {
		return this.prefMaxHeight;
	}

	public void setPrefMaxHeight( int value ) {
		this.prefMaxHeight = value;
		setButtons( this.buttons );
	}

	public int getMinButtonWidth() {
		return this.minWidth;
	}

	public void setMinWidth( int value ) {
		this.minWidth = value;
		setButtons( this.buttons );
	}
}
