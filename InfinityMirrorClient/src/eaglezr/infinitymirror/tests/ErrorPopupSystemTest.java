package eaglezr.infinitymirror.tests;

import eaglezr.infinitymirror.support.Error;
import eaglezr.infinitymirror.support.ErrorPopupSystem;
import eaglezr.infinitymirror.support.IMLoggingTool;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ErrorPopupSystemTest extends Application {

	public static void main( String[] args ) {
		Application.launch( args );
	}

	@Override public void start( Stage primaryStage ) throws Exception {
		//////////////////////////////
		// Setup
		//////////////////////////////
		Pane pane = new Pane();
		Scene scene = new Scene( pane );
		primaryStage.setScene( scene );
		ErrorPopupSystem.setLogger( IMLoggingTool
				.startLogger( IMLoggingTool.UserTypes.TEST, new Label( "" ), IMLoggingTool.Printers.CONSOLE_PRINTER ) );
		primaryStage.show();

		//////////////////////////////
		// Test with no set defaultStage
		//////////////////////////////
		try {
			ErrorPopupSystem.displayMessage( "Test error 1" );
		} catch ( NullPointerException e ) {
			System.out.println( "Correct error thrown" );
		}

		ErrorPopupSystem.displayMessage( "Test error 2", primaryStage );

		try {
			ErrorPopupSystem.displayError( Error.TEST );
		} catch ( NullPointerException e ) {
			System.out.println( "Correct error thrown" );
		}

		ErrorPopupSystem.displayError( Error.TEST, primaryStage );

		//////////////////////////////
		// Test with a set defaultStage
		//////////////////////////////
		ErrorPopupSystem.setDefaultStage( primaryStage );

		try {
			ErrorPopupSystem.displayMessage( "Test error 3" );
		} catch ( NullPointerException e ) {
			System.out.println( "Error should not have been thrown" );
		}

		ErrorPopupSystem.displayMessage( "Test error 4", primaryStage );

		try {
			ErrorPopupSystem.displayError( Error.TEST );
		} catch ( NullPointerException e ) {
			System.out.println( "Error should not have been thrown" );
		}

		ErrorPopupSystem.displayError( Error.TEST, primaryStage );
	}
}
