
package eaglezr.infinitymirror.support;

import eaglezr.support.LoggingTool;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Used for creating error pop-up windows above the provided {@link Stage}.
 * 
 * @author Mark Zeagler
 *
 */
public class ErrorManagementSystem {
	
	private static Stage stage;
	private static LoggingTool logger = LoggingTool.getLogger();
	
	public static void setStage( Stage setStage ) {
		stage = setStage;
	}
	
	public static void setLogger(LoggingTool setLogger) {
		logger = setLogger;
	}
	
	/**
	 * Displays an error over the given {@link javafx.stage.Stage}
	 * 
	 * @param stage
	 * @param error
	 */
	public static void displayError( Error error, Stage stage ) {
		logger.print( error.getText() );
		Label errorLabel = new Label( error.toString() );
		Button errorCloseButton = new Button( "Close" );
		
		BorderPane buttonPane = new BorderPane();
		buttonPane.setCenter( errorCloseButton );
		
		BorderPane errorPane = new BorderPane();
		errorPane.setCenter( errorLabel );
		errorPane.setBottom( buttonPane );
		
		Scene scene = new Scene( errorPane, error.toString().length() * 9, 75 );
		
		PopupStage errorPopup = new PopupStage(scene, stage);
		errorCloseButton.setOnAction( new ErrorClose( errorPopup ) );
		errorPopup.show();
	}
	
	/**
	 * Displays an error over the set {@link javafx.stage.Stage}
	 * 
	 * @param error
	 */
	public static void displayError( Error error ) {
		logger.print( error.getText() );
		Label errorLabel = new Label( error.toString() );
		Button errorCloseButton = new Button( "Close" );
		
		BorderPane buttonPane = new BorderPane();
		buttonPane.setCenter( errorCloseButton );
		
		BorderPane errorPane = new BorderPane();
		errorPane.setCenter( errorLabel );
		errorPane.setBottom( buttonPane );
		
		Scene scene = new Scene( errorPane, error.toString().length() * 9, 75 );
		
		Stage errorPopup = new Stage();
		errorCloseButton.setOnAction( new ErrorClose( errorPopup ) );
		errorPopup.setScene( scene );
		errorPopup.initModality( Modality.APPLICATION_MODAL );
		errorPopup.initOwner( stage );
		errorPopup.initStyle( StageStyle.UTILITY );
		errorPopup.resizableProperty().setValue( false );
		errorPopup.show();
	}
	
	// Closes the error pop-up message
	private static class ErrorClose implements EventHandler<ActionEvent> {
		Stage parent;
		
		public ErrorClose( Stage parent ) {
			this.parent = parent;
		}
		
		@Override
		public void handle( ActionEvent event ) {
			parent.close();
		}
	}
}
