import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InfinityMirrorDesktopClient extends Application {
	
	private boolean lightsOn = true;
	private Stage primaryStage;
	private MenuItem menuTurnOnLights;
	private MenuItem menuTurnOffLights;
	private Button onOffButton;
	private Button whiteLightModeButton;
	private int port = 11896;
	private String url = "192.168.1.158";
	private Color primaryColor = Color.BLUE; // Might want another to
												// differentiate between modes
	private Color secondaryColor = Color.RED;
	
	private Panes panes;
	
	EventHandler displayMainMenu;
	
	private class Panes {
		public Pane mainPane;
		public Pane solidColorModePane;
		public Pane desktopHarmonyModePane;
		public Pane soundResponsiveModePane;
		public Pane musicResponsiveModePane;
		
		public Panes() {
			this.mainPane = buildMainMenuPane();
			this.solidColorModePane = buildSolidColorModePane();
			this.desktopHarmonyModePane = buildDesktopHarmonyModePane();
			this.soundResponsiveModePane = buildSoundResponsiveModePane();
			this.musicResponsiveModePane = buildMusicResponsiveModePane();
		}
		
		private Pane buildMainMenuPane() {
			// Create instructional label
			Label instructionalLabel = new Label( "Select the desired mode: " );
			
			// Create modal control buttons
			Button solidColorModeButton = new Button( "Solid Color Mode" );
			solidColorModeButton.setOnAction( event -> displayPane( panes.solidColorModePane ) );
			
			Button desktopHarmonyModeButton = new Button( "Desktop Visual \nHarmony Mode" );
			desktopHarmonyModeButton.setOnAction( event -> displayPane( panes.desktopHarmonyModePane ) );
			
			Button soundResponsiveModeButton = new Button( "Sound Responsive Mode" );
			soundResponsiveModeButton.setOnAction( event -> displayPane( panes.soundResponsiveModePane ) );
			
			Button musicResponsiveModeButton = new Button( "Music Responsive Mode" );
			musicResponsiveModeButton.setOnAction( event -> displayPane( panes.musicResponsiveModePane ) );
			
			// Add modal control buttons to modal control pane
			GridPane modalControlPane = new GridPane();
			
			modalControlPane.add( solidColorModeButton, 0, 0 );
			solidColorModeButton.prefWidthProperty().bind( modalControlPane.widthProperty().divide( 2.0 ) );
			desktopHarmonyModeButton.prefHeightProperty().bind( modalControlPane.heightProperty().divide( 2.0 ) );
			
			modalControlPane.add( desktopHarmonyModeButton, 1, 0 );
			desktopHarmonyModeButton.prefWidthProperty().bind( modalControlPane.widthProperty().divide( 2.0 ) );
			solidColorModeButton.prefHeightProperty().bind( modalControlPane.heightProperty().divide( 2.0 ) );
			
			modalControlPane.add( soundResponsiveModeButton, 0, 1 );
			soundResponsiveModeButton.prefWidthProperty().bind( modalControlPane.widthProperty().divide( 2.0 ) );
			soundResponsiveModeButton.prefHeightProperty().bind( modalControlPane.heightProperty().divide( 2.0 ) );
			
			modalControlPane.add( musicResponsiveModeButton, 1, 1 );
			musicResponsiveModeButton.prefWidthProperty().bind( modalControlPane.widthProperty().divide( 2.0 ) );
			musicResponsiveModeButton.prefHeightProperty().bind( modalControlPane.heightProperty().divide( 2.0 ) );
			
			// Create primary control buttons
			whiteLightModeButton = new Button( "Toggle White Light" );
			whiteLightModeButton.setOnAction( new ToggleWhiteLightMode() );
			onOffButton = ( lightsOn ? new Button( "Turn Off Lights" ) : new Button( "Turn On Lights" ) );
			onOffButton.setOnAction( new ToggleLightsOnOff() );
			
			// Add primary control buttons to bottom pane
			GridPane primaryControlPane = new GridPane();
			
			primaryControlPane.add( whiteLightModeButton, 0, 0 );
			whiteLightModeButton.prefWidthProperty().bind( primaryControlPane.widthProperty().divide( 2.0 ) );
			whiteLightModeButton.prefHeightProperty().bind( primaryControlPane.heightProperty() );
			
			primaryControlPane.add( onOffButton, 1, 0 );
			onOffButton.prefWidthProperty().bind( primaryControlPane.widthProperty().divide( 2.0 ) );
			onOffButton.prefHeightProperty().bind( primaryControlPane.heightProperty() );
			
			// Create Application Pane
			BorderPane mainMenuPane = new BorderPane();
			mainMenuPane.setTop( instructionalLabel );
			mainMenuPane.setCenter( modalControlPane );
			mainMenuPane.setBottom( primaryControlPane );
			
			return mainMenuPane;
		}
		
		private Pane buildSubMenuPane( Pane subMenuContentPane ) {
			Button backButton = new Button( "<-- Back" );
			backButton.setOnAction( event -> displayPane(panes.mainPane) );
			BorderPane subMenuPane = new BorderPane();
			subMenuPane.setTop( backButton );
			subMenuPane.setCenter( subMenuContentPane );
			return subMenuPane;
		}
		
		private Pane buildSolidColorModePane() {
			BorderPane contentPane = new BorderPane();
			ColorPicker colorPicker = new ColorPicker();
			colorPicker.setOnAction( event -> primaryColor = colorPicker.getValue() );
			contentPane.setTop( colorPicker );
			Button startSolidColorModeButton = new Button( "Start Solid Color Mode" );
			startSolidColorModeButton.setOnAction( event -> sendCommand( 3 ) );
			contentPane.setBottom( startSolidColorModeButton );
			return buildSubMenuPane( contentPane );
		}
		
		private Pane buildDesktopHarmonyModePane() {
			Pane contentPane = new Pane();
			return buildSubMenuPane( contentPane ); // TODO
		}
		
		private Pane buildSoundResponsiveModePane() {
			Pane contentPane = new Pane();
			return buildSubMenuPane( contentPane ); // TODO
		}
		
		private Pane buildMusicResponsiveModePane() {
			Pane contentPane = new Pane();
			return buildSubMenuPane( contentPane ); // TODO
		}
		
	}
	
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
		displayMainMenu = event -> displayPane(panes.mainPane);
		
		this.primaryStage = stage;
		this.panes = new Panes();
		displayPane( panes.solidColorModePane );
	}
	
	private MenuBar buildMenuBar() {
		// Create Menus
		Menu fileMenu = new Menu( "File" );
		
		MenuItem returnToMain = new MenuItem( "Main Menu" );
		returnToMain.setOnAction( event -> displayPane(panes.mainPane) );
		
		MenuItem menuWhiteLightMode = new MenuItem( "Toggle White Light" );
		menuWhiteLightMode.setOnAction( new ToggleWhiteLightMode() );
		
		menuTurnOnLights = new MenuItem( "Turn on lights" );
		menuTurnOnLights.setOnAction( new ToggleLightsOnOff() );
		menuTurnOnLights.setVisible( false );
		
		menuTurnOffLights = new MenuItem( "Turn off lights" );
		menuTurnOffLights.setOnAction( new ToggleLightsOnOff() );
		
		MenuItem menuExit = new MenuItem( "Exit" );
		fileMenu.getItems().addAll( returnToMain, menuWhiteLightMode, menuTurnOnLights, menuTurnOffLights, menuExit );
		
		Menu optionsMenu = new Menu( "Options" );
		// TODO add stuffs
		
		Menu helpMenu = new Menu( "Help" );
		// TODO add stuffs
		
		// Create Menu Bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll( fileMenu, optionsMenu, helpMenu );
		return menuBar;
	}
	
	private void displayPane( Pane currentPane ) {
		// Build Application Pane
		BorderPane mainPane = new BorderPane();
		mainPane.setTop( buildMenuBar() );
		mainPane.setCenter( currentPane );
		
		// Display the scene
		Scene scene = new Scene( mainPane, 300, 300 );
		this.primaryStage.setScene( scene );
		this.primaryStage.setTitle( "Infinity Mirror Client" );
		this.primaryStage.resizableProperty().setValue( false );
		this.primaryStage.show();
	}
	
//	private class MainMenuSelected implements EventHandler<ActionEvent> {
//		
//		@Override
//		public void handle( ActionEvent event ) {
//			displayPane( panes.mainPane );
//		}
//	}
	
	private class ToggleLightsOnOff implements EventHandler<ActionEvent> {
		
		@Override
		public void handle( ActionEvent event ) {
			int response = sendCommand( 1 );
			if ( response == -1 ) {
				displayError( "E2: Command not sent" );
			} else if ( response / 10 != 1 ) {
				displayError( "E3: Received Incorrect Response: " + response );
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
		}
	}
	
	private class ToggleWhiteLightMode implements EventHandler<ActionEvent> {
		
		@Override
		public void handle( ActionEvent event ) {
			// TODO do something
		}
	}
	
	private class ErrorClose implements EventHandler<ActionEvent> {
		
		Stage parent;
		
		public ErrorClose( Stage parent ) {
			this.parent = parent;
		}
		
		@Override
		public void handle( ActionEvent event ) {
			parent.close();
		}
	}
	
	private int sendCommand( int command ) { // TODO app freezes if no response.
												// Find a way to handle
												// (threads?)
		int response = -1;
		try { // Any
			Socket socket = new Socket( url, port );
			DataInputStream in = new DataInputStream( socket.getInputStream() );
			DataOutputStream out = new DataOutputStream( socket.getOutputStream() );
			out.write( command );
			response = in.read();
			socket.close();
		} catch ( IOException e ) {
			displayError( "E1: Connection not made" );
		}
		return response;
	}
	
	private void displayError( String message ) {
		Label errorLabel = new Label( message );
		Button errorCloseButton = new Button( "Close" );
		
		BorderPane buttonPane = new BorderPane();
		buttonPane.setCenter( errorCloseButton );
		
		BorderPane errorPane = new BorderPane();
		errorPane.setCenter( errorLabel );
		errorPane.setBottom( buttonPane );
		
		Scene scene = new Scene( errorPane, message.length() * 9, 75 );
		
		Stage errorPopup = new Stage();
		errorCloseButton.setOnAction( new ErrorClose( errorPopup ) );
		errorPopup.setScene( scene );
		errorPopup.initModality( Modality.APPLICATION_MODAL );
		errorPopup.initOwner( primaryStage );
		errorPopup.initStyle( StageStyle.UTILITY );
		errorPopup.resizableProperty().setValue( false );
		errorPopup.show();
	}
}
