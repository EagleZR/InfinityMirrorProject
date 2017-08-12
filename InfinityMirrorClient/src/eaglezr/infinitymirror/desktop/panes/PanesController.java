package eaglezr.infinitymirror.desktop.panes;

import eaglezr.infinitymirror.desktop.ClientController;
import eaglezr.infinitymirror.support.IMLoggingTool;
import eaglezr.infinitymirror.support.InfinityMirror;
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

	// LATER Is there a way to make these build themselves automatically based on the panes I build?
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
		IMLoggingTool.print( "Building pane to display." );
		if ( this.currPane == this.mainMenu ) {
			IMLoggingTool.print( "Current pane is the main menu." );
			this.shellPane.updateContainer( currPane );
			currPane.prefWidthProperty().bind( shellPane.widthProperty() );
			currPane.prefHeightProperty().bind( shellPane.heightProperty() );
		} else {
			IMLoggingTool.print( "Current pane is the " + currPane.getClass().getSimpleName() + "." );
			this.shellPane.updateContainer( this.subMenuWrapper );
			this.subMenuWrapper.updateContainer( this.currPane );

			// LATER Maybe find a way to do this just once on setup?
			this.subMenuWrapper.prefWidthProperty().bind( shellPane.widthProperty() );
			this.subMenuWrapper.prefHeightProperty().bind( shellPane.heightProperty() );
		}
		return this.shellPane;
	}

	private void initializeEventHandlers() {
		// Navigation events
		this.displayMainMenu = event -> {
			IMLoggingTool.print( "Displaying Main Menu." );
			this.currPane = mainMenu;
			controller.updatePanes();
		};

		this.displaySolidColorPane = event -> {
			IMLoggingTool.print( "Displaying Solid MyColor Menu." );
			this.currPane = this.solidColor;
			controller.updatePanes();
		};

		this.displayAlternatingColorPane = event -> {
			IMLoggingTool.print( "Displaying Alternating MyColor Menu." );
			this.currPane = this.alternateColor;
			controller.updatePanes();
		};

		this.displayRainbowPane = event -> {
			IMLoggingTool.print( "Displaying Rainbow Menu." );
			this.currPane = this.rainbow;
			controller.updatePanes();
		};

		this.displayRainbowPulsePane = event -> {
			IMLoggingTool.print( "Displaying Rainbow Pulse Menu." );
			this.currPane = this.rainbowPulse;
			controller.updatePanes();
		};

		this.displayPulsePane = event -> {
			IMLoggingTool.print( "Displaying Pulse Menu." );
			this.currPane = this.pulse;
			controller.updatePanes();
		};

		// Functional events
		this.toggleLights = event -> {
			IMLoggingTool.print( "Toggling lights." );
			InfinityMirror newMirror = controller.currMirror.getToggleLights();
			controller.pushMirror( newMirror );
		};

		this.toggleWhiteLight = event -> {
			IMLoggingTool.print( "Toggling white lights." );
			InfinityMirror newMirror = controller.currMirror.getToggleWhiteLightMode();
			controller.pushMirror( newMirror );
		};

		this.pushMirror = event -> {
			IMLoggingTool.print( "Pushing " + this.currPane.getClass().getSimpleName() );
		};

		// Control Events
		this.displayHelp = event -> {
			IMLoggingTool.print( "Displaying Help menu." );
		};

		this.displaySettings = event -> {
			IMLoggingTool.print( "Displaying Settings menu." );
		};

		this.exit = event -> {
			IMLoggingTool.print( "" );
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
		// Solid MyColor Panel
		///////////////////

		// Solid MyColor navigation

		// Solid MyColor functionality

		///////////////////
		// Alternate MyColor Panel
		///////////////////

		// Alternate MyColor navigation

		// Alternate MyColor functionality

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

}
