
package eaglezr.infinitymirror.desktop.panes;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SolidColorPane extends Pane {
	
	protected ColorPicker colorPicker;
	protected Color primaryColor;
	protected Button startSolidColorModeButton;
	
	protected SolidColorPane() {
		BorderPane contentPane = new BorderPane();
		colorPicker = new ColorPicker();
		colorPicker.setOnAction( event -> primaryColor = colorPicker.getValue() );
		contentPane.setTop( colorPicker );
		startSolidColorModeButton = new Button( "Start Solid MyColor Mode" );
		// startSolidColorModeButton.setOnAction(event -> {
		// sendMessage(ClientCommands.SOLID_COLOR_MODE.COMMAND);
		// // TODO Find out how to transmit colors
		// });
		contentPane.setBottom( startSolidColorModeButton );
		super.getChildren().add( contentPane );
	}
}
