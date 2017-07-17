
package eaglezr.infinitymirror.client;

import eaglezr.infinitymirror.client.panes.*;
import eaglezr.infinitymirror.support.*;
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
	
	public ClientController( Stage stage ) {
		this.primaryStage = stage;
		ems = new ErrorManagementSystem (stage);
		this.panes = new PanesController( this, log, ems );
		displayPane(panes.getPane());
	}
	
	public void displayPane( Pane currentPane ) {
		// Display the scene
		Scene scene = new Scene( currentPane, 300, 400 );
		currentPane.prefHeightProperty().bind( scene.heightProperty() );
		System.out.println( currentPane.getHeight() );
		this.primaryStage.setScene( scene );
		this.primaryStage.setTitle( "Infinity Mirror Client" );
		this.primaryStage.resizableProperty().setValue( false );
		this.primaryStage.show();
	}
	
	public void sendMessage( int message ) {
		
	}
	
	public void updatePanes() {
		
	}
}
