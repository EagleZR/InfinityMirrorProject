package eaglezr.infinitymirror.desktop;

import eaglezr.infinitymirror.communications.ClientCommunicator;
import eaglezr.infinitymirror.desktop.panes.*;
import eaglezr.infinitymirror.support.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ClientController {

	private PanesController panes;
	private Stage primaryStage;
	public InfinityMirror currMirror;
	private ClientCommunicator communicator;

	public ClientController( Stage stage ) {
		communicator = new ClientCommunicator( "192.168.1.158", 11896 );
		IMLoggingTool
				.startLogger( IMLoggingTool.UserTypes.CLIENT, new Label( "" ), IMLoggingTool.Printers.LABEL_PRINTER );
		this.primaryStage = stage;
		ErrorPopupSystem.setDefaultStage( stage );
		ErrorPopupSystem.setLogger( IMLoggingTool.getLogger() );
		this.panes = new PanesController( this );
		Scene scene = new Scene( panes.getPane(), 300, 400 );

		this.primaryStage.setScene( scene );
		this.primaryStage.setTitle( "Infinity Mirror Client" );
		this.primaryStage.resizableProperty().setValue( false );
		this.primaryStage.show();

		updatePanes();
	}

	public void pushMirror( InfinityMirror mirror ) {
		communicator.pushMirror( mirror );
	}

	public void updatePanes() {
		primaryStage.getScene().setRoot( panes.getPane() );
	}

	public void exit() {
		Platform.exit();
	}
}
