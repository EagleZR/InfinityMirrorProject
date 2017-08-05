
package eaglezr.infinitymirror.desktop;

import eaglezr.infinitymirror.desktop.panes.*;
import eaglezr.infinitymirror.support.*;
import eaglezr.support.LoggingTool;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ClientController {
	
	Label outputLabel = new Label( "" );
	LoggingTool log = IMLoggingTool.startLogger( IMLoggingTool.UserTypes.CLIENT, outputLabel,
			IMLoggingTool.IM_Printers.LABEL_PRINTER );
	PanesController panes;
	Stage primaryStage;
	public InfinityMirror currMirror;
	
	public ClientController( Stage stage ) {
		this.primaryStage = stage;
		ErrorManagementSystem.setStage( stage );
		ErrorManagementSystem.setLogger( log );
		this.panes = new PanesController( this );
		Scene scene = new Scene( panes.getPane(), 300, 400 );
		this.primaryStage.setScene( scene );
		this.primaryStage.setTitle( "Infinity Mirror Client" );
		this.primaryStage.resizableProperty().setValue( false );
		this.primaryStage.show();
		updatePanes();
	}
	
	public void updateMirror() {
		
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
