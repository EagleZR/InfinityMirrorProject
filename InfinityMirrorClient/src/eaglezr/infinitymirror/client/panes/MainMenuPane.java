package eaglezr.infinitymirror.client.panes;

import eaglezr.infinitymirror.client.ClientController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainMenuPane extends IMPane {
	
	public Button solidColorModeButton;
	public Button alternatingColorModeButton;
	public Button rainbowModeButton;
	public Button rainbowPulseModeButton;
	public Button pulseModeButton;
	public Button onOffButton;
	public Button whiteLightModeButton;

	public MainMenuPane(ClientController controller) {
		super(controller);
		// Create instructional label
		Label instructionalLabel = new Label("Select the desired mode: ");

		// Create modal control buttons
		solidColorModeButton = new Button("Solid Color Mode");
		//solidColorModeButton.setOnAction(event -> new SolidColorPane(controller).display());

		alternatingColorModeButton = new Button("Alternating Color Mode");
		//alternatingColorModeButton.setOnAction(event -> new AlternatingColorPane(controller).display());

		rainbowModeButton = new Button("Rainbow Mode");
		//rainbowModeButton.setOnAction(event -> new RainbowPane(controller).display());

		rainbowPulseModeButton = new Button("Rainbow Pulse Mode");
		//rainbowPulseModeButton.setOnAction(event -> new RainbowPulsePane(controller).display());

		pulseModeButton = new Button("Pulse Mode");
		//pulseModeButton.setOnAction(event -> new PulsePane(controller).display());

		// Add modal control buttons to modal control pane
		ButtonPane menuPane = new ButtonPane(solidColorModeButton, alternatingColorModeButton, rainbowModeButton,
				rainbowPulseModeButton, pulseModeButton);

		// Create primary control buttons
		whiteLightModeButton = new Button("Toggle White Light");
//		whiteLightModeButton.setOnAction(toggleWhiteLight);
		onOffButton = (controller.getLightsOn() ? new Button("Turn Off Lights") : new Button("Turn On Lights"));
//		onOffButton.setOnAction(toggleLights);

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
		mainMenuPane.setCenter(menuPane);
		mainMenuPane.setBottom(primaryControlPane);

		super.container = mainMenuPane;
	}
	
	
}
