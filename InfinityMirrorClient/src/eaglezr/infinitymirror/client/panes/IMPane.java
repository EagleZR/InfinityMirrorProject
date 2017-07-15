package eaglezr.infinitymirror.client.panes;

import eaglezr.infinitymirror.support.ErrorManagementSystem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class IMPane extends Pane {
	
	/**
	 * Add all content to be displayed in here.
	 */
	public Label outputLabel;
	public MyMenuBar menu;
	
	BorderPane innerPane;
	
	protected IMPane( ErrorManagementSystem ems ) {
		outputLabel = ems.getLabel();
		
		// Initialize GUI
		BorderPane shell = buildPaneShell();
		this.getChildren().add( shell );
	}
	
	public void updateContainer(Pane pane) {
		innerPane.setCenter( pane );
	}
	
	private BorderPane buildPaneShell() {
		// Build Application Pane
		BorderPane mainPane = new BorderPane();
		mainPane.setTop( this.menu = new MyMenuBar() );
		innerPane = new BorderPane();
		innerPane.setTop( buildInfinityMirrorPane() );
		mainPane.setCenter( innerPane );
		mainPane.setBottom( outputLabel );
		return mainPane;
	}
	
	/**
	 * # of LEDs per vertical part: 18
	 * <p>
	 * # of LEDs per horizontal part: 72
	 * <p>
	 * Total # of LEDs: 180
	 */
	private Pane buildInfinityMirrorPane() {
		// Initialize Mirror
		Rectangle[] lights = new Rectangle[180];
		Color[] colors = new Color[180];
		for ( int i = 0; i < lights.length; i++ ) {
			lights[i] = new Rectangle();
		}
		
		BorderPane pane = new BorderPane();
		Image image = new Image( "file:eaglezr\\infinitymirror\\resources\\images\\MirrorBase.png", true );
		ImageView imageView = new ImageView( image );
		pane.setCenter( imageView );
//		EventHandler<ActionEvent> eventHandler = e -> {
//			// offsetColor();
//			// TODO colorLights(paneType);
//		};
//		
//		Timeline changingLights = new Timeline( new KeyFrame( Duration.millis( 250 ), eventHandler ) );
		
		return pane;
	}
	
	protected class MyMenuBar extends MenuBar {
		
		public MenuItem returnToMain;
		public MenuItem whiteLightMode;
		public MenuItem turnOnLights;
		public MenuItem turnOffLights;
		public MenuItem exit;
		
		protected MyMenuBar() {
			// Create Menus
			Menu fileMenu = new Menu( "File" );
			
			returnToMain = new MenuItem( "Main Menu" );
			// returnToMain.setOnAction(this.displayMainMenu);
			
			whiteLightMode = new MenuItem( "Toggle White Light" );
			// menuWhiteLightMode.setOnAction(toggleWhiteLight);
			
			turnOnLights = new MenuItem( "Turn on lights" );
			// menuTurnOnLights.setOnAction(toggleLights);
			turnOnLights.setVisible( false );
			
			turnOffLights = new MenuItem( "Turn off lights" );
			// menuTurnOffLights.setOnAction(toggleLights);
			
			exit = new MenuItem( "Exit" );
			fileMenu.getItems().addAll( returnToMain, whiteLightMode, turnOnLights, turnOffLights,
					exit );
			
			Menu optionsMenu = new Menu( "Options" );
			// TODO Add Options Menu components
			
			Menu helpMenu = new Menu( "Help" );
			// TODO Add Help Menu components
			
			// Create Menu Bar
			this.getMenus().addAll( fileMenu, optionsMenu, helpMenu );
		}
		
	}
	
	public void toggleLights( boolean isOn ) {
		menu.turnOnLights.setVisible( isOn );
		menu.turnOffLights.setVisible( isOn );
	}
	
	public void toggleWhiteLight( boolean isWhite ) {
		
	}
	
}