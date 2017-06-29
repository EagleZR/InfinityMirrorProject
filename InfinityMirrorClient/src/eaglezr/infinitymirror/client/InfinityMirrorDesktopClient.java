
package eaglezr.infinitymirror.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.AccessControlException;
import eaglezr.infinitymirror.client.panes.*;
import eaglezr.infinitymirror.support.ClientCommands;
import eaglezr.infinitymirror.support.ErrorManagementSystem;
import eaglezr.infinitymirror.support.ErrorManagementSystem.Printers;
import eaglezr.infinitymirror.support.ErrorManagementSystem.UserTypes;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InfinityMirrorDesktopClient extends Application {
	
	private boolean lightsOn = true;
	private Stage primaryStage;
	private int port = 11896;
	private String url = "136.59.71.218";
	protected ClientController controller;
	protected EventHandler<ActionEvent> toggleLights;
	protected EventHandler<ActionEvent> toggleWhiteLight;
	protected EventHandler<ActionEvent> displayMainMenu;
	private ErrorManagementSystem.Printers defaultPrinter = ErrorManagementSystem.Printers.CONSOLE_PRINTER;
	// private ErrorManagementSystem ems = new
	// ErrorManagementSystem(ErrorManagementSystem.UserTypes.CLIENT,
	// outputLabel, defaultPrinter);
	
	/**
	 * Included for Eclipse
	 * 
	 * @param args
	 */
	public static void main( String[] args ) {
		Application.launch( args );
	}
	
	@Override
	public void start( Stage stage ) throws Exception {
		initializePanes();
		
		initializeEventHandlers();
		
		
		
		this.controller = new ClientController();
		
		this.primaryStage = stage;
		displayPane( new MainMenuPane( controller ) );
	}
	
	private void initializePanes() {
		
	}
	
	private void initializeEventHandlers() {
		displayMainMenu = event -> displayPane( new MainMenuPane( this.controller ) );
		
		toggleLights = event -> {
			sendMessage( ClientCommands.LIGHTS.COMMAND );
		};
		
		toggleWhiteLight = event -> {
			sendMessage( ClientCommands.WHITE_MODE.COMMAND );
		};
	}
	
	private void displayPane( Pane currentPane ) {
		// Build Application Pane
		BorderPane mainPane = new BorderPane();
		mainPane.setTop( buildMenuBar() );
		BorderPane innerPane = new BorderPane();
		innerPane.setTop( panes.infinityMirrorPane );
		innerPane.setCenter( currentPane );
		mainPane.setCenter( innerPane );
		mainPane.setBottom( outputLabel );
		
		// Display the scene
		Scene scene = new Scene( mainPane, 300, 400 );
		this.primaryStage.setScene( scene );
		this.primaryStage.setTitle( "Infinity Mirror Client" );
		this.primaryStage.resizableProperty().setValue( false );
		this.primaryStage.show();
	}
	
	private void parseServerResponse( int command, int response ) {
		if ( command == ClientCommands.LIGHTS.COMMAND ) {
			if ( response == -1 ) {
				ems.displayError( primaryStage, "E2: Command not sent" );
			} else if ( response / 10 != 1 ) {
				ems.displayError( primaryStage, "E3: Received Incorrect Response: " + response );
			} else if ( response % 10 == 0 ) { // Lights are now off
				lightsOn = false;
				menuTurnOnLights.setVisible( true );
				menuTurnOffLights.setVisible( false );
				onOffButton.setText( "Turn On Lights" );
			} else if ( response % 10 == 1 ) { // Lights are now on
				lightsOn = true;
				menuTurnOnLights.setVisible( false );
				menuTurnOffLights.setVisible( true );
				onOffButton.setText( "Turn Off Lights" );
			}
		} // TODO Respond to other commands
		else {
			ems.displayError( primaryStage, "E6: Command Parsing Error" );
		}
	}
	
	private void sendMessage( int message ) {
		// FIXME Integrate with new communications package
	}
}
