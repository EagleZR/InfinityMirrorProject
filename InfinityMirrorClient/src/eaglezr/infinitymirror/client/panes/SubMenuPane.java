package eaglezr.infinitymirror.client.panes;

import eaglezr.infinitymirror.support.ErrorManagementSystem;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class SubMenuPane extends Pane {
	
	public Button backButton;
	public Pane container;
	
	private BorderPane subMenuPane;
	
	protected SubMenuPane(ErrorManagementSystem ems) {
		// Initialize GUI
		backButton = new Button("<-- Back");
		subMenuPane = new BorderPane();
		subMenuPane.setTop(backButton);
		updateContainer(container);
		super.getChildren().add( subMenuPane );
	}
	
	public void updateContainer(Pane pane) {
		subMenuPane.setCenter( pane );
	}
	
}