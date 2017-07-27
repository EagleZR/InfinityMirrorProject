package eaglezr.javafx.panes;

import eaglezr.support.LoggingTool;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * A {@link javafx} pane made for displaying a {@link GridPane} of {@link Button} objects that expands in both the
 * horizontal and vertical directions based on a set of definitions. <p>The grid of buttons will expand until it hits a
 * maximum Y value, but will not expand into the Y direction until it has met the minimum expansion in the X
 * direction.</p> <p>The X direction is horizontal when the {@link Orientation} is horizontal and is vertical when the
 * {@link Orientation} is vertical.</p><p>The Y direction is vertical when the {@link Orientation} is horizontal and is
 * horizontal when the {@link Orientation} is vertical.</p>
 *
 * @author Mark Zeagler
 * @version 0.9
 */
public class ButtonPane extends GridPane {

	/**
	 * The orientations determine the impacts of the X and the Y values. In Horizontal orientation, X corresponds to the
	 * horizontal and Y corresponds to the vertical. In Vertical orientation, X corresponds to the vertical and Y to the
	 * horizontal.
	 */
	public enum Orientation {
		Horizontal, Vertical
	}

	int minXRetraction = 2;
	int maxYExpansion = 3;

	Button[] buttons = {};

	Orientation orientation = Orientation.Horizontal;

	/**
	 * Creates an empty ButtonPane.
	 */
	public ButtonPane() {

	}

	/**
	 * Creates a ButtonPane of the specified buttons.
	 *
	 * @param buttons
	 */
	public ButtonPane( Button... buttons ) {
		setButtons( buttons );
	}

	/**
	 * Creates a ButtonPane of the specified {@link Orientation} and dimmensions.
	 *
	 * @param minXRetraction
	 * @param maxYExpansion
	 * @param orientation
	 * @param buttons
	 */
	public ButtonPane( int minXRetraction, int maxYExpansion, Orientation orientation, Button... buttons ) {
		this.orientation = orientation;
		this.minXRetraction = minXRetraction;
		this.maxYExpansion = maxYExpansion;
		setButtons( buttons );
	}

	/**
	 * Clears the old set of {@link Button}s and rebuilds the pane with the provided list.
	 *
	 * @param buttons
	 */
	public void setButtons( Button... buttons ) {
		this.buttons = buttons;
		super.getChildren().clear();

		int width = width();
		int height = height();

		System.out.println( "New Button Layout of width: " + width + " and height: " + height );

		int u = 0;
		// LATER There's probably a more elegant way to do this, but it works for now...
		if ( orientation == Orientation.Vertical ) {
			for ( int x = 0; x < width && u < buttons.length; x++ ) {
				for ( int y = 0; y < height && u < buttons.length; y++, u++ ) {
					super.add( buttons[u], x, y );
					buttons[u].prefHeightProperty().bind( this.heightProperty().divide( height ) );
					buttons[u].prefWidthProperty().bind( this.widthProperty().divide( width ) );
				}
			}
		} else {
			for ( int x = 0; x < height && u < buttons.length; x++ ) {
				for ( int y = 0; y < width && u < buttons.length; y++, u++ ) {
					super.add( buttons[u], y, x );
					buttons[u].prefHeightProperty().bind( this.heightProperty().divide( height ) );
					buttons[u].prefWidthProperty().bind( this.widthProperty().divide( width ) );
				}
			}
		}
	}

	private int width() {
		return ( orientation == Orientation.Horizontal ? getA() : getB() );
	}

	private int height() {
		return ( orientation == Orientation.Horizontal ? getB() : getA() );
	}

	private int getA() {
		return ( buttons.length <= minXRetraction * maxYExpansion ?
				minXRetraction :
				(int) Math.ceil( buttons.length / (double) maxYExpansion + ( buttons.length % maxYExpansion == 0 ?
						0 :
						1 ) ) );
	}

	// LATER Messy, might just run a calculation method that stores into either an array or into class variables
	private int getB() {
		int a = getA();
		return ( buttons.length / a > 0 ? buttons.length / a + ( buttons.length % a == 0 ? 0 : 1 ) : 1 );
	}

	/**
	 * Adds the provided {@link Button} to the pane.
	 *
	 * @param button
	 */
	public void addButton( Button button ) {
		addButtons( button );
	}

	/**
	 * Adds the given {@link Button} array to the pane.
	 *
	 * @param buttons
	 */
	public void addButtons( Button... buttons ) {
		Button[] newButtons = new Button[this.buttons.length + buttons.length];
		int i = 0;
		boolean nullFlag = false;
		for ( ; i < this.buttons.length; i++ ) {
			newButtons[i] = this.buttons[i];
		}
		for ( int u = 0; u < buttons.length; u++ ) {
			if ( buttons[u] != null ) {
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

	/**
	 * Removes the indicated {@link Button} from the pane.
	 *
	 * @param button
	 */
	public void removeButton( Button button ) {
		removeButtons( button );
	}

	/**
	 * Removes the provided {@link Button} array from the pane.
	 *
	 * @param buttons
	 */
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

	/**
	 * Returns the {@link Button} array currently displayed on the pane.
	 *
	 * @return
	 */
	public Button[] getButtons() {
		return this.buttons;
	}

	/**
	 * Returns the max expansion in the Y direction. <p>NOTE: The direction of expansion is determined by the stored
	 * {@link Orientation}.</p>
	 *
	 * @return
	 */
	public int getMaxYExpansion() {
		return this.maxYExpansion;
	}

	/**
	 * Sets the max expansion in the Y direction. <p>NOTE: The direction of expansion is determined by the stored {@link
	 * Orientation}.</p>
	 *
	 * @param value
	 */
	public void setMaxYExpansion( int value ) {
		if ( value > 0 ) {
			this.maxYExpansion = value;
			setButtons( this.buttons );
		} else {
			LoggingTool.generateConsolePrinter()
					.accept( this.getClass().getName() + "Negative Values are not accepted." );
		}
	}

	/**
	 * Gets the minimum retraction in the X Direction. <p>NOTE: The direction of expansion is determined by the stored
	 * {@link Orientation}.</p>
	 *
	 * @return
	 */
	public int getMinXRetraction() {
		return this.minXRetraction;
	}

	/**
	 * Sets the minimum retraction in the X direction. <p>NOTE: The direction of expansion is determined by the stored
	 * {@link Orientation}.</p>
	 *
	 * @param value
	 */
	public void setMinXRetraction( int value ) {
		if ( value > 0 ) {
			this.minXRetraction = value;
			setButtons( this.buttons );
		} else {
			LoggingTool.generateConsolePrinter()
					.accept( this.getClass().getName() + "Negative Values are not accepted." );
		}
	}

	/**
	 * Retrieves the current {@link Orientation} of the pane's expansion.
	 *
	 * @return
	 */
	public Orientation getOrientation() {
		return this.orientation;
	}

	/**
	 * Sets the {@link Orientation} of the pane's expansion.
	 *
	 * @param orientation
	 */
	public void setOrientation( Orientation orientation ) {
		this.orientation = orientation;
		setButtons( this.buttons );
	}
}
