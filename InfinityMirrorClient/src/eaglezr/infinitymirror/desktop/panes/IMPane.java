package eaglezr.infinitymirror.desktop.panes;

import eaglezr.infinitymirror.support.IMLoggingTool;
import eaglezr.support.logs.LoggingTool;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class IMPane extends BorderPane {

	/**
	 * Add all content to be displayed in here.
	 */
	protected Label outputLabel;
	protected MyMenuBar menu;

	protected BorderPane innerPane;

	private String mirrorAddress = "eaglezr\\infinitymirror\\resources\\images\\MirrorBase.png";

	protected IMPane() {
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
	 * <p># of LEDs per vertical part: 18 </p><p> # of LEDs per horizontal part: 72</p><p> Total # of LEDs: 180</p>
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

		// File
		protected MenuItem load;
		protected MenuItem save;
		protected MenuItem exit;

		// View
		protected MenuItem goToMain;
		protected MenuItem goToAltColor;

		// Options
		protected MenuItem whiteLightMode;
		protected MenuItem turnOnLights;
		protected MenuItem turnOffLights;

		// Help
		protected MenuItem about;

		protected MyMenuBar() {
			// File
			Menu fileMenu = new Menu( "File" );

			load = new MenuItem( "Load Favorite..." );
			save = new MenuItem( "Save Favorite..." );
			exit = new MenuItem( "Exit" );

			fileMenu.getItems().addAll( load, save, exit );

			// View
			Menu viewMenu = new Menu( "View" );
			Menu goToViewMenu = new Menu( "Go To" );

			goToMain = new MenuItem( "Main Menu" );
			goToAltColor = new MenuItem( "Alternating Color" );
			// TODO Add other MenuItems to the View tab

			goToViewMenu.getItems().addAll( goToMain, goToAltColor );
			viewMenu.getItems().addAll( goToViewMenu );

			// Options
			Menu optionsMenu = new Menu( "Options" );

			whiteLightMode = new MenuItem( "Toggle White Light" );
			turnOnLights = new MenuItem( "Turn on lights" );
			turnOnLights.setVisible( false );
			turnOffLights = new MenuItem( "Turn off lights" );

			optionsMenu.getItems().addAll( whiteLightMode, turnOnLights, turnOffLights );

			// Help
			Menu helpMenu = new Menu( "Help" );

			about = new MenuItem( "About..." );

			helpMenu.getItems().addAll( about );

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
