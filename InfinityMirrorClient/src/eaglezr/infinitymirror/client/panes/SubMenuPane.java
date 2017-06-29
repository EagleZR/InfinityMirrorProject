package eaglezr.infinitymirror.client.panes;

import eaglezr.infinitymirror.client.ClientController;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public abstract class SubMenuPane extends IMPane {

	protected Color primaryColor = Color.BLUE; // Might want another to
	protected Color secondaryColor = Color.RED;
	protected Pane subMenuContainer;
	
	protected SubMenuPane(ClientController controller) {
		super(controller);
		
		// Initialize GUI
		Button backButton = new Button("<-- Back");
//		backButton.setOnAction(super.displayMainMenu);
		BorderPane subMenuPane = new BorderPane();
		subMenuPane.setTop(backButton);
		subMenuPane.setCenter(subMenuContainer);
		super.container = subMenuPane;
	}
	
	
}
