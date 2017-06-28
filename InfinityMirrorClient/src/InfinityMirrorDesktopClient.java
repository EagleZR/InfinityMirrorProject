import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.AccessControlException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class InfinityMirrorDesktopClient extends Application {

	private static enum PaneTypes {
		SOLID_COLOR, ALTERNATING_COLOR, DESKTOP_HARMONY, SOUND_RESPONSIVE, MUSIC_RESPONSIVE
	};

	private boolean lightsOn = true;
	private Stage primaryStage;
	private MenuItem menuTurnOnLights;
	private MenuItem menuTurnOffLights;
	private Button onOffButton;
	private Button whiteLightModeButton;
	private Label outputLabel = new Label("");
	private int port = 11896;
	private String url = "136.59.71.218";
	private Color primaryColor = Color.BLUE; // Might want another to
	private Color secondaryColor = Color.RED;

	private Panes panes;

	EventHandler<ActionEvent> displayMainMenu;
	EventHandler<ActionEvent> toggleLights;
	EventHandler<ActionEvent> toggleWhiteLight;

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
		
		

		public Pane mainPane;
		public Pane solidColorModePane;
		public Pane alternatingColorModePane;
		public Pane rainbowModePane;
		public Pane rainbowPulseModePane;
		public Pane pulseModePane;
		public Pane infinityMirrorPane;

		public Panes() {
			for (int i = 0; i < lights.length; i++) {
				lights[i] = new Rectangle();
			}

			this.mainPane = buildMainMenuPane();
			this.solidColorModePane = buildSolidColorModePane();
			this.alternatingColorModePane = buildAlternatingColorModePane();
			this.rainbowModePane = buildRainbowModePane();
			this.rainbowPulseModePane = buildRainbowPulseModePane();
			this.pulseModePane = buildPulseModePane();
			this.infinityMirrorPane = buildInfinityMirrorPane();
		}

		private Pane buildMainMenuPane() {
			// Create instructional label
			Label instructionalLabel = new Label("Select the desired mode: ");

			// Create modal control buttons
			Button solidColorModeButton = new Button("Solid Color Mode");
			solidColorModeButton.setOnAction(event -> displayPane(panes.solidColorModePane));

			Button alternatingColorModeButton = new Button("Alternating Color Mode");
			alternatingColorModeButton.setOnAction(event -> displayPane(panes.alternatingColorModePane));

			Button rainbowModeButton = new Button("Rainbow Mode");
			rainbowModeButton.setOnAction(event -> displayPane(panes.rainbowModePane));

			Button rainbowPulseModeButton = new Button("Rainbow Pulse Mode");
			rainbowPulseModeButton.setOnAction(event -> displayPane(panes.rainbowPulseModePane));
			
			Button pulseModeButton = new Button(" Pulse Mode");
			pulseModeButton.setOnAction(event -> displayPane(panes.pulseModePane));

			// Add modal control buttons to modal control pane
			GridPane modalControlPane = new GridPane();

			modalControlPane.add(solidColorModeButton, 0, 0);
			solidColorModeButton.prefWidthProperty().bind(modalControlPane.widthProperty().divide(2.0));
			solidColorModeButton.prefHeightProperty().bind(modalControlPane.heightProperty().divide(2.0));

			modalControlPane.add(alternatingColorModeButton, 1, 0);
			alternatingColorModeButton.prefWidthProperty().bind(modalControlPane.widthProperty().divide(2.0));
			alternatingColorModeButton.prefHeightProperty().bind(modalControlPane.heightProperty().divide(2.0));

			modalControlPane.add(rainbowModeButton, 0, 1);
			rainbowModeButton.prefWidthProperty().bind(modalControlPane.widthProperty().divide(2.0));
			rainbowModeButton.prefHeightProperty().bind(modalControlPane.heightProperty().divide(2.0));

			modalControlPane.add(rainbowPulseModeButton, 1, 1);
			rainbowPulseModeButton.prefWidthProperty().bind(modalControlPane.widthProperty().divide(2.0));
			rainbowPulseModeButton.prefHeightProperty().bind(modalControlPane.heightProperty().divide(2.0));
			
			// TODO Add Pulse Mode button
			// TODO Create ButtonPane extends Pane ?

			// Create primary control buttons
			whiteLightModeButton = new Button("Toggle White Light");
			whiteLightModeButton.setOnAction(toggleWhiteLight);
			onOffButton = (lightsOn ? new Button("Turn Off Lights") : new Button("Turn On Lights"));
			onOffButton.setOnAction(toggleLights);

			// Add primary control buttons to bottom pane
			GridPane primaryControlPane = new GridPane();

			primaryControlPane.add(whiteLightModeButton, 0, 0);
			whiteLightModeButton.prefWidthProperty().bind(primaryControlPane.widthProperty().divide(2.0));
			whiteLightModeButton.prefHeightProperty().bind(primaryControlPane.heightProperty());

			primaryControlPane.add(onOffButton, 1, 0);
			onOffButton.prefWidthProperty().bind(primaryControlPane.widthProperty().divide(2.0));
			onOffButton.prefHeightProperty().bind(primaryControlPane.heightProperty());

			// Create Application Pane
			BorderPane mainMenuPane = new BorderPane();
			mainMenuPane.setTop(instructionalLabel);
			mainMenuPane.setCenter(modalControlPane);
			mainMenuPane.setBottom(primaryControlPane);

			return mainMenuPane;
		}

		private Pane buildSubMenuPane(Pane subMenuContentPane) {
			Button backButton = new Button("<-- Back");
			backButton.setOnAction(event -> displayPane(panes.mainPane));
			BorderPane subMenuPane = new BorderPane();
			subMenuPane.setTop(backButton);
			subMenuPane.setCenter(subMenuContentPane);
			return subMenuPane;
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

		private Pane buildAlternatingColorModePane() {
			Pane contentPane = new Pane();
			// TODO Build Alternating Color Control Pane
			return buildSubMenuPane(contentPane); 
		}

		private Pane buildRainbowModePane() {
			Pane contentPane = new Pane();
			// TODO Build Rainbow Mode Control Pane
			return buildSubMenuPane(contentPane);
		}

		private Pane buildRainbowPulseModePane() {
			Pane contentPane = new Pane();
			// TODO Build Rainbow Mode Control Pane
			return buildSubMenuPane(contentPane); 
		}
		
		private Pane buildPulseModePane() {
			Pane contentPane = new Pane();
			// TODO Build Pulse Mode Control Pane
			return buildSubMenuPane(contentPane); 
		}

		private Pane buildInfinityMirrorPane() {
			BorderPane pane = new BorderPane();
			Image image = new Image("MirrorBase.png");
			ImageView imageView = new ImageView (image);
			pane.setCenter(imageView);
			EventHandler<ActionEvent> eventHandler = e -> {
				offsetColor();
				// TODO colorLights(paneType);
			};

			Timeline changingLights = new Timeline(new KeyFrame(Duration.millis(250), eventHandler));

			return pane;
		}

		private void colorLights(int paneType) {

		}

		private void offsetColor() {

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

		this.primaryStage = stage;
		this.panes = new Panes();
		displayPane(panes.mainPane);
	}

	private MenuBar buildMenuBar() {
		// Create Menus
		Menu fileMenu = new Menu("File");

		MenuItem returnToMain = new MenuItem("Main Menu");
		returnToMain.setOnAction(event -> displayPane(panes.mainPane));

		MenuItem menuWhiteLightMode = new MenuItem("Toggle White Light");
		menuWhiteLightMode.setOnAction(toggleWhiteLight);

		menuTurnOnLights = new MenuItem("Turn on lights");
		menuTurnOnLights.setOnAction(toggleLights);
		menuTurnOnLights.setVisible(false);

		menuTurnOffLights = new MenuItem("Turn off lights");
		menuTurnOffLights.setOnAction(toggleLights);

		MenuItem menuExit = new MenuItem("Exit");
		fileMenu.getItems().addAll(returnToMain, menuWhiteLightMode, menuTurnOnLights, menuTurnOffLights, menuExit);

		Menu optionsMenu = new Menu("Options");
		// TODO add stuffs

		Menu helpMenu = new Menu("Help");
		// TODO add stuffs

		// Create Menu Bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, optionsMenu, helpMenu);
		return menuBar;
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

//	private int sendCommand(int command) {
//		// TODO app freezes if no response. Find a way to handle (threads?)
//		int response = -1;
//		try { // Any
//			Socket socket = new Socket(url, port);
//			DataInputStream in = new DataInputStream(socket.getInputStream());
//			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//			out.write(command);
//			response = in.read();
//			socket.close();
//		} catch (IOException e) {
//			displayError("E1: Connection not made");
//		}
//		return response;
//	}

//	private class MessagingService extends Service<Integer> {
//
//		private int command;
//
//		public int getCommand() {
//			return this.command;
//		}
//
//		public void setCommand(int value) {
//			this.command = value;
//		}
//
//		@Override
//		protected Task<Integer> createTask() {
//			return new Task<Integer>() {
//
//				@Override
//				protected Integer call() throws Exception {
//					outputLabel.setText("Sending Command: " + command);
//					int response = -1;
//					try { // Any
//						Socket socket = new Socket(url, port);
//						DataInputStream in = new DataInputStream(socket.getInputStream());
//						DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//						out.write(command);
//						response = in.read();
//						socket.close();
//					} catch (IOException e) {
//						displayError("E1: Connection not made");
//					}
//					return response;
//				}
//
//			};
//		}
//
//	}
	
	private void sendMessage(int command) {
		try {
			SendMessageTask sendMessage = new SendMessageTask(command);
			Thread thread = new Thread(sendMessage);
			
			thread.setDaemon(false);
			thread.start();
		} catch (Exception e) {
			displayError("E1: Connection not made");
		}
	}

	private class SendMessageTask extends Task {

		private int command;
		private int response;
		
		public SendMessageTask(int command) {
			this.command = command;
		}

		@Override
		protected Object call() throws Exception {
			response = -1;
			try { // Any
				Socket socket = new Socket(url, port);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				connectToServer();
				out.write(command);
				response = in.read();
				socket.close();
			} catch (IOException e) {
				displayError("E1: Connection not made");
			} catch (AccessControlException e) {
				displayError("E5: Connection Rejected");
			}
			return response; // TODO Determine if possible to avoid returning anything
		}

		@Override
		protected void succeeded() {
			super.succeeded();
			parseServerResponse(command, response);
		}
		
		@Override
		protected void failed() {
			super.failed();
			displayError("E4: Communications Thread Failed");
		}
	}
	
	private void parseServerResponse(int command, int response) {
		if (command == ClientCommands.LIGHTS.COMMAND) {
			if (response == -1) {
				displayError("E2: Command not sent");
			} else if (response / 10 != 1) {
				displayError("E3: Received Incorrect Response: " + response);
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
		} // TODO respond to other commands
		else {
			displayError ("E6: Command Parsing Error");
		}
	}
	
	private void connectToServer() throws AccessControlException {
		
	}

	/*
	 * private class SendMessageThread extends Thread {
	 * 
	 * int message;
	 * 
	 * public SendMessageThread(int message) { this.message = message; }
	 * 
	 * @Override public void run() { int response = -1; try { // Any Socket
	 * socket = new Socket(url, port); DataInputStream in = new
	 * DataInputStream(socket.getInputStream()); DataOutputStream out = new
	 * DataOutputStream(socket.getOutputStream()); out.write(this.message);
	 * response = in.read(); socket.close(); } catch (IOException e) {
	 * displayError("E1: Connection not made"); }
	 * 
	 * if (response == -1) { displayError("E2: Command not sent"); } else if
	 * (response / 10 != 1) { displayError("E3: Received Incorrect Response: " +
	 * response); } else if (response % 10 == 0) { // Lights are now off
	 * lightsOn = false; menuTurnOnLights.setVisible(true);
	 * menuTurnOffLights.setVisible(false);
	 * onOffButton.setText("Turn On Lights"); } else if (response % 10 == 1) {
	 * // Lights are now on lightsOn = true; menuTurnOnLights.setVisible(false);
	 * menuTurnOffLights.setVisible(true);
	 * onOffButton.setText("Turn Off Lights"); } } }
	 */

	//
	// Error Message System
	//

	private void displayError(String message) {
		Label errorLabel = new Label(message);
		Button errorCloseButton = new Button("Close");

		BorderPane buttonPane = new BorderPane();
		buttonPane.setCenter(errorCloseButton);

		BorderPane errorPane = new BorderPane();
		errorPane.setCenter(errorLabel);
		errorPane.setBottom(buttonPane);

		Scene scene = new Scene(errorPane, message.length() * 9, 75);

		Stage errorPopup = new Stage();
		errorCloseButton.setOnAction(new ErrorClose(errorPopup));
		errorPopup.setScene(scene);
		errorPopup.initModality(Modality.APPLICATION_MODAL);
		errorPopup.initOwner(primaryStage);
		errorPopup.initStyle(StageStyle.UTILITY);
		errorPopup.resizableProperty().setValue(false);
		errorPopup.show();
	}

	private class ErrorClose implements EventHandler<ActionEvent> {
		Stage parent;

		public ErrorClose(Stage parent) {
			this.parent = parent;
		}

		@Override
		public void handle(ActionEvent event) {
			parent.close();
		}
	}
}
