
package eaglezr.infinitymirror.desktop;

import eaglezr.infinitymirror.desktop.panes.*;
import eaglezr.infinitymirror.support.*;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientController {
	
	Label outputLabel = new Label( "" );
	LoggingTool log = LoggingTool.startLogger( LoggingTool.UserTypes.CLIENT, outputLabel,
			LoggingTool.Printers.LABEL_PRINTER );
	ErrorManagementSystem ems;
	PanesController panes;
	Stage primaryStage;
	public InfinityMirror currMirror;
	
	public ClientController( Stage stage ) {
		this.primaryStage = stage;
		ems = new ErrorManagementSystem (stage);
		this.panes = new PanesController( this, log, ems );
		Scene scene = new Scene( panes.getPane(), 300, 400 );
		this.primaryStage.setScene( scene );
		this.primaryStage.setTitle( "Infinity Mirror Client" );
//		this.primaryStage.resizableProperty().setValue( false );
		this.primaryStage.show();
		updatePanes();
	}
	
	public void pushMirror ( InfinityMirror mirror ) {
		
	}
	
	public void updatePanes() {
		primaryStage.getScene().setRoot( panes.getPane() );
	}
	
	public void exit() {
		Platform.exit();
	}
}
