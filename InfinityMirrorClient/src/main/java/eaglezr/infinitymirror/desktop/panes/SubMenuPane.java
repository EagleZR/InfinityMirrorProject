
package eaglezr.infinitymirror.desktop.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class SubMenuPane extends Pane {
	
	protected Button backButton;
	protected Pane container;
	protected Button pushButton;
	
	private BorderPane subMenuPane;
	
	protected SubMenuPane() {
		// Initialize GUI
		backButton = new Button( "<-- Back" );
		pushButton = new Button( "Push Changes" );
		subMenuPane = new BorderPane();
		subMenuPane.setTop( backButton );
		subMenuPane.setBottom( pushButton );
		updateContainer( container );
		super.getChildren().add( subMenuPane );
	}
	
	protected void updateContainer( Pane pane ) {
		subMenuPane.setCenter( pane );
	}
	
}
