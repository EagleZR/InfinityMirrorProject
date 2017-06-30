package eaglezr.infinitymirror.client.panes;

import eaglezr.infinitymirror.client.ClientController;
import eaglezr.infinitymirror.support.ErrorManagementSystem;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SolidColorPane extends Pane {
	
	public ColorPicker colorPicker;
	public Color primaryColor;
	public Button startSolidColorModeButton;

	public SolidColorPane(ErrorManagementSystem ems) {
		BorderPane contentPane = new BorderPane();
		colorPicker = new ColorPicker();
		colorPicker.setOnAction(event -> primaryColor = colorPicker.getValue());
		contentPane.setTop(colorPicker);
		startSolidColorModeButton = new Button("Start Solid Color Mode");
//		startSolidColorModeButton.setOnAction(event -> {
//			sendMessage(ClientCommands.SOLID_COLOR_MODE.COMMAND);
//			// TODO Find out how to transmit colors
//		});
		contentPane.setBottom(startSolidColorModeButton);
		super.getChildren().add( contentPane );
	}
}
