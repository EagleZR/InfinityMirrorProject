
package eaglezr.infinitymirror.desktop.panes;

import eaglezr.infinitymirror.support.ErrorManagementSystem;
import eaglezr.infinitymirror.support.LoggingTool;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class SubMenuPane extends Pane {
	
	protected Button backButton;
	protected Pane container;
	
	private BorderPane subMenuPane;
	
	protected SubMenuPane( LoggingTool log, ErrorManagementSystem ems ) {
		// Initialize GUI
		backButton = new Button( "<-- Back" );
		subMenuPane = new BorderPane();
		subMenuPane.setTop( backButton );
		updateContainer( container );
		super.getChildren().add( subMenuPane );
	}
	
	protected void updateContainer( Pane pane ) {
		subMenuPane.setCenter( pane );
	}
	
}
