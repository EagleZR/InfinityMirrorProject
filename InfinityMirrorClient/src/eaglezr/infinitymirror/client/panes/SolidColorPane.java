package eaglezr.infinitymirror.client.panes;

import eaglezr.infinitymirror.client.ClientController;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;

public class SolidColorPane extends SubMenuPane {
	
	public ColorPicker colorPicker;
	public Button startSolidColorModeButton;

	public SolidColorPane(ClientController controller) {
		super(controller);
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
		super.subMenuContainer.getChildren().add( contentPane );
	}
}
