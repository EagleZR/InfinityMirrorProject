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
	protected EventHandler<ActionEvent> toggleLights;
	protected EventHandler<ActionEvent> toggleWhiteLight;
	protected EventHandler<ActionEvent> displayMainMenu;
	private ErrorManagementSystem.Printers defaultPrinter = ErrorManagementSystem.Printers.CONSOLE_PRINTER; 
	private ErrorManagementSystem ems = new ErrorManagementSystem(ErrorManagementSystem.UserTypes.CLIENT, outputLabel, defaultPrinter);

	private class Panes {

		/**
		 * # of LEDs per vertical part: 18
		 * <p>
		 * # of LEDs per horizontal part: 72
		 * <p>
		 * Total # of LEDs: 180
		 */
		Rectangle[] lights = new Rectangle[180];
		Color[] colors = new Color[180];

		public Panes() {
			for (int i = 0; i < lights.length; i++) {
				lights[i] = new Rectangle();
			}

		}

		private Pane buildSolidColorModePane() {
			BorderPane contentPane = new BorderPane();
			ColorPicker colorPicker = new ColorPicker();
			colorPicker.setOnAction(event -> primaryColor = colorPicker.getValue());
			contentPane.setTop(colorPicker);
			Button startSolidColorModeButton = new Button("Start Solid Color Mode");
			startSolidColorModeButton.setOnAction(event -> {
				sendMessage(ClientCommands.SOLID_COLOR_MODE.COMMAND);
				// TODO Find out how to transmit colors
			});
			contentPane.setBottom(startSolidColorModeButton);
			return buildSubMenuPane(contentPane);
		}
	}

	/**
	 * Included for Eclipse
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		displayMainMenu = event -> displayPane(panes.mainPane);

		toggleLights = event -> {
			sendMessage(ClientCommands.LIGHTS.COMMAND);
		};

		toggleWhiteLight = event -> {
			sendMessage(ClientCommands.WHITE_MODE.COMMAND);
		};
		

		// TODO Create the other button events

		ClientController controller = new ClientController();
		
		this.primaryStage = stage;
		new MainMenuPane(stage, controller).display();
	}

	private void displayPane(Pane currentPane) {
		// Build Application Pane
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(buildMenuBar());
		BorderPane innerPane = new BorderPane();
		innerPane.setTop(panes.infinityMirrorPane);
		innerPane.setCenter(currentPane);
		mainPane.setCenter(innerPane);
		mainPane.setBottom(outputLabel);

		// Display the scene
		Scene scene = new Scene(mainPane, 300, 400);
		this.primaryStage.setScene(scene);
		this.primaryStage.setTitle("Infinity Mirror Client");
		this.primaryStage.resizableProperty().setValue(false);
		this.primaryStage.show();
	}

	private void parseServerResponse(int command, int response) {
		if (command == ClientCommands.LIGHTS.COMMAND) {
			if (response == -1) {
				ems.displayError(primaryStage, "E2: Command not sent");
			} else if (response / 10 != 1) {
				ems.displayError(primaryStage, "E3: Received Incorrect Response: " + response);
			} else if (response % 10 == 0) { // Lights are now off
				lightsOn = false;
				menuTurnOnLights.setVisible(true);
				menuTurnOffLights.setVisible(false);
				onOffButton.setText("Turn On Lights");
			} else if (response % 10 == 1) { // Lights are now on
				lightsOn = true;
				menuTurnOnLights.setVisible(false);
				menuTurnOffLights.setVisible(true);
				onOffButton.setText("Turn Off Lights");
			}
		} // TODO Respond to other commands
		else {
			ems.displayError(primaryStage, "E6: Command Parsing Error");
		}
	}

	private void sendMessage(int message) {
		// FIXME Integrate with new communications package
	}
}
