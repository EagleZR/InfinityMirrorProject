
package eaglezr.infinitymirror.desktop.panes;

import eaglezr.infinitymirror.desktop.ClientController;
import eaglezr.infinitymirror.support.ClientCommands;
import eaglezr.infinitymirror.support.ErrorManagementSystem;
import eaglezr.infinitymirror.support.InfinityMirror;
import eaglezr.infinitymirror.support.IMLoggingTool;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;

public class PanesController {
	
	private ClientController controller;
	
	private IMPane shellPane;
	private MainMenuPane mainMenu;
	private SubMenuPane subMenuWrapper;
	private SolidColorPane solidColor;
	private AlternatingColorPane alternateColor;
	private RainbowPane rainbow;
	private RainbowPulsePane rainbowPulse;
	private PulsePane pulse;
	
	// Functional events
	private EventHandler<ActionEvent> toggleLights;
	private EventHandler<ActionEvent> toggleWhiteLight;
	private EventHandler<ActionEvent> pushMirror;
	
	// LATER Is there a way to make these build themselves automatically based on
	// the panes I build?
	// Navigation events
	private EventHandler<ActionEvent> displayMainMenu;
	private EventHandler<ActionEvent> displaySolidColorPane;
	private EventHandler<ActionEvent> displayAlternatingColorPane;
	private EventHandler<ActionEvent> displayRainbowPane;
	private EventHandler<ActionEvent> displayRainbowPulsePane;
	private EventHandler<ActionEvent> displayPulsePane;
	
	// Control events
	private EventHandler<ActionEvent> displaySettings;
	private EventHandler<ActionEvent> displayHelp;
	private EventHandler<ActionEvent> exit;
	
	private Pane currPane;
	
	public PanesController( ClientController controller ) {
		this.controller = controller;
		
		this.shellPane = new IMPane();
		this.mainMenu = new MainMenuPane();
		this.subMenuWrapper = new SubMenuPane();
		this.solidColor = new SolidColorPane();
		this.alternateColor = new AlternatingColorPane();
		this.rainbow = new RainbowPane();
		this.rainbowPulse = new RainbowPulsePane();
		this.pulse = new PulsePane();
		
		this.currPane = this.mainMenu;
		
		initializeEventHandlers();
		
		addEventHandlers();
	}
	
	// LATER Change this so it builds the panel each time
	public Pane getPane() {
		if ( this.currPane == this.mainMenu ) {
			this.shellPane.updateContainer( currPane );
			currPane.prefWidthProperty().bind( shellPane.widthProperty() );
			currPane.prefHeightProperty().bind( shellPane.heightProperty() );
		} else {
			this.shellPane.updateContainer( this.subMenuWrapper );
			this.subMenuWrapper.updateContainer( this.currPane );
			
			this.subMenuWrapper.prefWidthProperty().bind( shellPane.widthProperty() );
			this.subMenuWrapper.prefHeightProperty().bind( shellPane.heightProperty() );
			
//			currPane.prefWidthProperty().bind( this.subMenuWrapper.widthProperty() );
//			currPane.prefHeightProperty().bind( this.subMenuWrapper.heightProperty() );
		}
		return this.shellPane;
	}
	
	private void initializeEventHandlers() {
		// Navigation events
		this.displayMainMenu = event -> {
			this.currPane = mainMenu;
			controller.updatePanes();
		};
		
		this.displaySolidColorPane = event -> {
			this.currPane = this.solidColor;
			controller.updatePanes();
		};
		
		this.displayAlternatingColorPane = event -> {
			this.currPane = this.alternateColor;
			controller.updatePanes();
		};
		
		this.displayRainbowPane = event -> {
			this.currPane = this.rainbow;
			controller.updatePanes();
		};
		
		this.displayRainbowPulsePane = event -> {
			this.currPane = this.rainbowPulse;
			controller.updatePanes();
		};
		
		this.displayPulsePane = event -> {
			this.currPane = this.pulse;
			controller.updatePanes();
		};
		
		// Functional events
		this.toggleLights = event -> {
			// TODO Find a way to store previous or something so toggling
			// actually toggles
			// controller.pushMirror( new InfinityMirror(InfinityMirror.Mode.)
			// );
			// controller.sendMessage( ClientCommands.LIGHTS.COMMAND );
		};
		
		this.toggleWhiteLight = event -> {
			
			// controller.sendMessage( ClientCommands.WHITE_MODE.COMMAND );
		};
		
		this.pushMirror = event -> {
			IMLoggingTool.getLogger().print( "Pushing " + this.currPane.getClass().getSimpleName() );
		};
		
		// Control Events
		this.displayHelp = event -> {
			
		};
		
		this.displaySettings = event -> {
			
		};
		
		this.exit = event -> {
			this.controller.exit();
		};
	}
	
	private void addEventHandlers() {
		///////////////////
		// Shell
		///////////////////
		
		// Shell navigation
		this.shellPane.menu.goToMain.setOnAction( displayMainMenu );
		this.shellPane.menu.goToAltColor.setOnAction( this.displayAlternatingColorPane );
		
		// Shell functionality
		this.shellPane.menu.turnOnLights.setOnAction( this.toggleLights );
		this.shellPane.menu.turnOffLights.setOnAction( this.toggleLights );
		this.shellPane.menu.whiteLightMode.setOnAction( this.toggleWhiteLight );
		this.shellPane.menu.exit.setOnAction( this.exit );
		
		// Shell control
		this.shellPane.menu.exit.setOnAction( this.exit );
		// TODO Add other Shell event handlers
		
		///////////////////
		// Wrapper
		///////////////////
		
		// Wrapper navigation
		this.subMenuWrapper.backButton.setOnAction( this.displayMainMenu );
		
		// Wrapper functionality
		this.subMenuWrapper.pushButton.setOnAction( this.pushMirror );
		
		///////////////////
		// Main Menu
		///////////////////
		
		// Main Menu navigation
		this.mainMenu.alternatingColorModeButton.setOnAction( this.displayAlternatingColorPane );
		this.mainMenu.solidColorModeButton.setOnAction( this.displaySolidColorPane );
		this.mainMenu.rainbowModeButton.setOnAction( this.displayRainbowPane );
		this.mainMenu.rainbowPulseModeButton.setOnAction( this.displayRainbowPulsePane );
		this.mainMenu.pulseModeButton.setOnAction( this.displayPulsePane );
		
		// Main Menu functionality
		this.mainMenu.onOffButton.setOnAction( this.toggleLights );
		this.mainMenu.whiteLightModeButton.setOnAction( this.toggleWhiteLight );
		
		///////////////////
		// Solid Color Panel
		///////////////////
		
		// Solid Color navigation
		
		// Solid Color functionality
		
		///////////////////
		// Alternate Color Panel
		///////////////////
		
		// Alternate Color navigation
		
		// Alternate Color functionality
		
		///////////////////
		// Rainbow Panel
		///////////////////
		
		// Rainbow navigation
		
		// Rainbow functionality
		
		///////////////////
		// Rainbow Pulse Panel
		///////////////////
		
		// Rainbow Pulse navigation
		
		// Rainbow Pulse functionality
		
		///////////////////
		// Pulse Panel
		///////////////////
		
		// Pulse navigation
		
		// Pulse functionality
		
	}
	
	public void toggleLights( boolean isOn ) {
		this.mainMenu.toggleLights( isOn );
		this.shellPane.toggleLights( isOn );
	}
	
	public void toggleWhiteLight( boolean isWhite ) {
		this.mainMenu.toggleWhiteLight( isWhite );
		this.shellPane.toggleWhiteLight( isWhite );
	}
	
}
