
package eaglezr.infinitymirror.client.panes;

import eaglezr.infinitymirror.client.ClientController;
import eaglezr.infinitymirror.support.ClientCommands;
import eaglezr.infinitymirror.support.ErrorManagementSystem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;

public class PanesController {
	
	ClientController controller;
	
	IMPane shellPane;
	MainMenuPane mainMenu;
	SubMenuPane subMenuWrapper;
	SolidColorPane solidColor;
	AlternatingColorPane alternateColor;
	RainbowPane rainbow;
	RainbowPulsePane rainbowPulse;
	PulsePane pulse;
	
	// Functional events
	protected EventHandler<ActionEvent> toggleLights;
	protected EventHandler<ActionEvent> toggleWhiteLight;
	
	// Navigation events
	protected EventHandler<ActionEvent> displayMainMenu;
	protected EventHandler<ActionEvent> displaySolidColorPane;
	protected EventHandler<ActionEvent> displayAlternatingColorPane;
	protected EventHandler<ActionEvent> displayRainbowPane;
	protected EventHandler<ActionEvent> displayRainbowPulsePane;
	protected EventHandler<ActionEvent> displayPulsePane;
	
	public Pane currPane;
	
	public PanesController( ClientController controller, ErrorManagementSystem ems ) {
		
		this.shellPane = new IMPane( ems );
		
		this.mainMenu = new MainMenuPane( ems );
		
		this.subMenuWrapper = new SubMenuPane( ems );
		
		this.solidColor = new SolidColorPane( ems );
		this.alternateColor = new AlternatingColorPane( ems );
		this.rainbow = new RainbowPane( ems );
		this.rainbowPulse = new RainbowPulsePane( ems );
		this.pulse = new PulsePane( ems );
		
		this.currPane = this.mainMenu;
		
		sizePanes();
		
		initializeEventHandlers();
		
		addEventHandlers();
	}
	
	public Pane getPane() {
		if ( this.currPane == this.mainMenu ) {
			this.shellPane.updateContainer( currPane );
		} else {
			this.shellPane.updateContainer( this.subMenuWrapper );
			this.subMenuWrapper.updateContainer( this.currPane );
		}
		return this.shellPane;
	}
	
	private void sizePanes() {
		
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
			controller.sendMessage( ClientCommands.LIGHTS.COMMAND );
		};
		
		this.toggleWhiteLight = event -> {
			controller.sendMessage( ClientCommands.WHITE_MODE.COMMAND );
		};
		
	}
	
	private void addEventHandlers() {
		// Shell navigation
		this.shellPane.menu.returnToMain.setOnAction( this.displayMainMenu );
		
		// Shell functionality
		this.shellPane.menu.turnOnLights.setOnAction( this.toggleLights );
		this.shellPane.menu.turnOffLights.setOnAction( this.toggleLights );
		this.shellPane.menu.whiteLightMode.setOnAction( this.toggleWhiteLight );
		
		// Wrapper navigation
		this.subMenuWrapper.backButton.setOnAction( this.displayMainMenu );
		
		// Wrapper functionality
		
		// Main Menu navigation
		this.mainMenu.alternatingColorModeButton.setOnAction( this.displayAlternatingColorPane );
		this.mainMenu.solidColorModeButton.setOnAction( this.displaySolidColorPane );
		this.mainMenu.rainbowModeButton.setOnAction( this.displayRainbowPane );
		this.mainMenu.rainbowPulseModeButton.setOnAction( this.displayRainbowPulsePane );
		this.mainMenu.pulseModeButton.setOnAction( this.displayPulsePane );
		
		// Main Menu functionality
		this.mainMenu.onOffButton.setOnAction( this.toggleLights );
		this.mainMenu.whiteLightModeButton.setOnAction( this.toggleWhiteLight );
		
		// Solid Color navigation
		
		// Solid Color functionality
		
		// Alternate Color navigation
		
		// Alternate Color functionality
		
		// Rainbow navigation
		
		// Rainbow functionality
		
		// Rainbow Pulse navigation
		
		// Rainbow Pulse functionality
		
		// Pulse navigation
		
		// Pulse functionality
		
	}
	
	public void toggleLights( boolean isOn ) {
		this.mainMenu.toggleLights( isOn );
		this.shellPane.toggleLights( isOn );
	}
	
	public void toggleWhiteLight(boolean isWhite) {
		this.mainMenu.toggleWhiteLight( isWhite );
		this.shellPane.toggleWhiteLight( isWhite );
	}
	
}
