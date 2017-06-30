
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
	ErrorManagementSystem.Printers defaultPrinter = ErrorManagementSystem.Printers.CONSOLE_PRINTER;
	ErrorManagementSystem ems = new ErrorManagementSystem( ErrorManagementSystem.UserTypes.CLIENT, outputLabel,
			defaultPrinter );
	PanesController panes;
	Stage primaryStage;
	
	public ClientController( Stage stage ) {
		this.primaryStage = stage;
		this.panes = new PanesController( this, ems );
		displayPane(panes.getPane());
	}
	
	public void displayPane( Pane currentPane ) {
		// Display the scene
		Scene scene = new Scene( currentPane, 300, 400 );
		this.primaryStage.setScene( scene );
		this.primaryStage.setTitle( "Infinity Mirror Client" );
		this.primaryStage.resizableProperty().setValue( false );
		this.primaryStage.show();
	}
	
	public boolean getLightsOn() {
		return true; // FIXME Program comms with the server
	}
	
	public void sendMessage( int message ) {
		
	}
	
	public class ListenerService extends Service {
		
		@Override
		protected Task createTask() {
			// TODO Create a task?
			return null;
		}
		
		@SuppressWarnings( "rawtypes" )
		public class ServerListenerTask extends Task {
			
			@Override
			protected Object call() throws Exception {
				// FIXME Listen for a signal from the server
				
				return null;
			}
			
			/**
			 * Update data from here
			 */
			protected void succeeded() {
				super.succeeded();
				// TODO Update according to server response
			}
			
			protected void failed() {
				super.failed();
				outputLabel.setText( "Could not update from the server" );
			}
		}
	}
	
	public void updatePanes() {
		
	}
}
