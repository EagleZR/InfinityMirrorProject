
package eaglezr.infinitymirror.desktop.panes;

import eaglezr.infinitymirror.support.ErrorManagementSystem;
import eaglezr.infinitymirror.support.IMLoggingTool;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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

public class IMPane extends BorderPane {
	
	/**
	 * Add all content to be displayed in here.
	 */
	protected Label outputLabel;
	protected MyMenuBar menu;
	
	protected BorderPane innerPane;
	
	private String mirrorAddress = "eaglezr\\infinitymirror\\resources\\images\\MirrorBase.png";
	
	protected IMPane( ErrorManagementSystem ems ) {
		IMLoggingTool log = IMLoggingTool.getLogger();
		if ( log != null ) {
			outputLabel = log.getLabel();
		}
		buildPaneShell();
	}
	
	protected void updateContainer( Pane pane ) {
		innerPane.setCenter( pane );
	}
	
	private void buildPaneShell() {
		// Build Application Pane
		this.setTop( this.menu = new MyMenuBar() );
		innerPane = new BorderPane();
		Pane mirrorPane = buildInfinityMirrorPane();
		innerPane.setTop( mirrorPane );
		this.setCenter( innerPane );
		this.setBottom( outputLabel );
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
		// Rectangle[] lights = new Rectangle[180];
		// for ( int i = 0; i < lights.length; i++ ) {
		// lights[i] = new Rectangle( 4, 4, Color.RED );
		// }
		
		BorderPane pane = new BorderPane();
		Image image = new Image( mirrorAddress, true );
		ImageView imageView = new ImageView( image );
		pane.setCenter( imageView );
		// EventHandler<ActionEvent> eventHandler = e -> {
		// // offsetColor();
		// // TODO colorLights(paneType);
		// };
		//
		// Timeline changingLights = new Timeline( new KeyFrame(
		// Duration.millis( 250 ), eventHandler ) );
		
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
			fileMenu.getItems().addAll( whiteLightMode, turnOnLights, turnOffLights, exit );
			
			// View
			Menu viewMenu = new Menu( "View" );
			
			goToMain = new MenuItem( "Main Menu" );
			
			Menu goToViewMenu = new Menu( "Go To..." );
			
			goToAltColor = new MenuItem( "Alternating Color" );
			
			goToViewMenu.getItems().addAll( goToMain, goToAltColor );
			
			viewMenu.getItems().addAll( goToViewMenu );
			
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
