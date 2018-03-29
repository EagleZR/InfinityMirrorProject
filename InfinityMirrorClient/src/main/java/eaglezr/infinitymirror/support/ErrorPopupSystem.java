package eaglezr.infinitymirror.support;

import eaglezr.javafx.stages.PopupStage;
import eaglezr.support.logs.LoggingTool;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Used for creating error pop-up windows above a provided or pre-defined {@link Stage}.
 *
 * @author Mark Zeagler
 */
public class ErrorPopupSystem {

	private static Stage defaultStage;
	private static LoggingTool logger;

	/**
	 * Sets the default {@link Stage} that the pop-up windows will be displayed over. This is useful for simplifying
	 * method calls when all of the windows will be over the same stage. <p>NOTE: Using the methods which have take a
	 * {@link Stage} as a parameter will not be affected by this.</p>
	 *
	 * @param setStage The {@link Stage} over which the pop-ups will be displayed unless a method is used which takes a
	 *                 {@link Stage} as a parameter.
	 */
	public static void setDefaultStage( Stage setStage ) {
		defaultStage = setStage;
	}

	/**
	 * Sets the {@link LoggingTool} to be printed to. All messages displayed in pop-ups will also be printed to the
	 * default log.
	 *
	 * @param setLogger The {@link LoggingTool} that the pop-up text will be echoed in.
	 */
	public static void setLogger( LoggingTool setLogger ) {
		logger = setLogger;
	}

	/**
	 * Displays a text message over the set {@link Stage}. <p>NOTE: The {@link Stage} over which the error is displayed will
	 * be locked from interaction until the pop-up is closed.</p>
	 * @param message The text to display in the pop-up window.
	 * @throws NullPointerException If there is no set {@link Stage}, running this method will throw an exception.
	 */
	public static void displayMessage( String message ) throws NullPointerException {
		displayMessage( message, defaultStage );
	}

	/**
	 * Displays a message over the given {@link Stage}. <p>NOTE: The {@link Stage} over which the error is displayed
	 * will be locked from interaction until the pop-up is closed.</p>
	 *
	 * @param message The text to display in the pop-up window.
	 * @param stage   The stage over which the error will be shown.
	 */
	public static void displayMessage( String message, Stage stage ) {
		logger.print( "Pop-up displayed: " + message );
		Label errorLabel = new Label( message );
		Button errorCloseButton = new Button( "Close" );

		BorderPane buttonPane = new BorderPane();
		buttonPane.setCenter( errorCloseButton );

		BorderPane errorPane = new BorderPane();
		errorPane.setCenter( errorLabel );
		errorPane.setBottom( buttonPane );

		Scene scene = new Scene( errorPane, message.length() * 9, 75 );

		PopupStage errorPopup = new PopupStage( scene, stage );
		errorCloseButton.setOnAction( new ErrorClose( errorPopup ) );
		errorPopup.show();
	}

	/**
	 * Displays an {@link Error} over the given {@link Stage}. <p>NOTE: The {@link Stage} over which the error is displayed will
	 * be locked from interaction until the pop-up is closed.</p>
	 *
	 * @param error The {@link Error} whose message will be displayed in the pop-up window.
	 * @param stage The stage over which the error will be shown.
	 */
	public static void displayError( Error error, Stage stage ) {
		displayMessage( error.toString(), stage );
	}

	/**
	 * Displays an {@link Error} over the set {@link Stage}. <p>NOTE: The {@link Stage} over which the error is displayed will
	 * be locked from interaction until the pop-up is closed.</p>
	 *
	 * @param error The {@link Error} whose message will be displayed in the pop-up window.
	 * @throws NullPointerException If there is no set {@link Stage}, running this method will throw an exception.
	 */
	public static void displayError( Error error ) throws NullPointerException {
		displayError( error, defaultStage );
	}

	// Closes the error pop-up message
	private static class ErrorClose implements EventHandler<ActionEvent> {
		Stage parent;

		ErrorClose( Stage parent ) {
			this.parent = parent;
		}

		@Override public void handle( ActionEvent event ) {
			parent.close();
		}
	}
}
