
package eaglezr.infinitymirror.desktop.panes;

import eaglezr.infinitymirror.support.ErrorManagementSystem;
import eaglezr.infinitymirror.support.LoggingTool;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainMenuPane extends Pane {
	
	protected Button solidColorModeButton;
	protected Button alternatingColorModeButton;
	protected Button rainbowModeButton;
	protected Button rainbowPulseModeButton;
	protected Button pulseModeButton;
	protected Button onOffButton;
	protected Button whiteLightModeButton;
	
	protected MainMenuPane( LoggingTool log, ErrorManagementSystem ems ) {
		// Create instructional label
		Label instructionalLabel = new Label( "Select the desired mode: " );
		
		// Create modal control buttons
		solidColorModeButton = new Button( "Solid Color Mode" );
		
		alternatingColorModeButton = new Button( "Alternating Color Mode" );
		
		rainbowModeButton = new Button( "Rainbow Mode" );
		
		rainbowPulseModeButton = new Button( "Rainbow Pulse Mode" );
		
		pulseModeButton = new Button( "Pulse Mode" );
		
		// Add modal control buttons to modal control pane
		ButtonPane menuPane = new ButtonPane( solidColorModeButton, alternatingColorModeButton, rainbowModeButton,
				rainbowPulseModeButton, pulseModeButton );
		
		// Create primary control buttons
		whiteLightModeButton = new Button( "Toggle White Light" );
		onOffButton = new Button( "Turn on lights" );
		
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
		menuPane.prefWidthProperty().bind( mainMenuPane.widthProperty() );
		mainMenuPane.setTop( instructionalLabel );
		mainMenuPane.setCenter( menuPane );
		mainMenuPane.setBottom( primaryControlPane );
		
		super.getChildren().add( mainMenuPane );
		mainMenuPane.prefWidthProperty().bind( super.widthProperty() );
		mainMenuPane.prefHeightProperty().bind( super.heightProperty() );
	}
	
	protected void toggleLights( boolean isOn ) {
		onOffButton = ( isOn ? new Button( "Turn Off Lights" ) : new Button( "Turn On Lights" ) );
	}
	
	protected void toggleWhiteLight( boolean isWhite ) {
		
	}
	
}
