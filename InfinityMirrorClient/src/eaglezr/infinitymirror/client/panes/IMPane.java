package eaglezr.infinitymirror.client.panes;

import eaglezr.infinitymirror.support.ErrorManagementSystem;
import eaglezr.infinitymirror.support.LoggingTool;
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
	protected Label outputLabel;
	protected MyMenuBar menu;
	
	protected BorderPane innerPane;
	
	protected IMPane( LoggingTool log, ErrorManagementSystem ems ) {
		outputLabel = log.getLabel();
		
		// Initialize GUI
		BorderPane shell = buildPaneShell();
		this.getChildren().add( shell );
	}
	
	protected void updateContainer(Pane pane) {
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
		
		protected MenuItem whiteLightMode;
		protected MenuItem turnOnLights;
		protected MenuItem turnOffLights;
		protected MenuItem exit;
		
		protected MenuItem goToMain;
		protected MenuItem goToAltColor;
		
		protected MyMenuBar() {
			// File
			Menu fileMenu = new Menu( "File" );
			
			whiteLightMode = new MenuItem( "Toggle White Light" );
			
			turnOnLights = new MenuItem( "Turn on lights" );
			turnOnLights.setVisible( false );
			
			turnOffLights = new MenuItem( "Turn off lights" );
			
			exit = new MenuItem( "Exit" );
			fileMenu.getItems().addAll( whiteLightMode, turnOnLights, turnOffLights,
					exit );
			
			// View
			Menu viewMenu = new Menu("View");
			
			goToMain = new MenuItem( "Main Menu" );
			
			Menu goToViewMenu = new Menu("Go To...");
			
			goToAltColor = new MenuItem( "Alternating Color" );
			
			goToViewMenu.getItems().addAll( goToMain, goToAltColor );
			
			viewMenu.getItems().addAll( goToViewMenu, goToMain );
			
			// TODO Add other MenuItems to the View tab
			
			// Options
			Menu optionsMenu = new Menu( "Options" );
			// TODO Add Options Menu components
			
			// Help
			Menu helpMenu = new Menu( "Help" );
			// TODO Add Help Menu components
			
			// Create Menu Bar
			this.getMenus().addAll( fileMenu, viewMenu, optionsMenu, helpMenu );
		}
		
	}
	
	protected void toggleLights( boolean isOn ) {
		menu.turnOnLights.setVisible( isOn );
		menu.turnOffLights.setVisible( isOn );
	}
	
	protected void toggleWhiteLight( boolean isWhite ) {
		
	}
	
}
