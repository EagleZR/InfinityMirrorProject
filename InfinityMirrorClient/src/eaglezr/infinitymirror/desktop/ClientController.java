package eaglezr.infinitymirror.desktop;

import eaglezr.infinitymirror.communications.ClientCommunicator;
import eaglezr.infinitymirror.desktop.panes.*;
import eaglezr.infinitymirror.support.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClientController {

	private PanesController panes;
	private Stage primaryStage;
	public InfinityMirror currMirror = new InfinityMirror( InfinityMirror.Mode.SOLID_COLOR_MODE, Color.RED );
	public InfinityMirrorPreview mirrorBuild = new InfinityMirrorPreview( currMirror );
	private ClientCommunicator communicator;

	public ClientController( Stage stage ) {
		IMLoggingTool
				.startLogger( IMLoggingTool.UserTypes.CLIENT, new Label( "" ), IMLoggingTool.Printers.LABEL_PRINTER );
		IMLoggingTool.print( "Initializing new InfinityMirror Desktop Client." );
		IMLoggingTool.print( "Initializing new ClientCommunicator." );
		communicator = new ClientCommunicator( "192.168.10.1", 11896 );
		this.primaryStage = stage;
		IMLoggingTool.print( "Initializing ErrorPopupSystem." );
		ErrorPopupSystem.setDefaultStage( stage );
		ErrorPopupSystem.setLogger( IMLoggingTool.getLogger() );
		IMLoggingTool.print( "Creating PanesController." );
		this.panes = new PanesController( this );
		IMLoggingTool.print( "Displaying Scene." );
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
		IMLoggingTool.print( "Updating Scene." );
		primaryStage.getScene().setRoot( panes.getPane() );
	}

	public void exit() {
		IMLoggingTool.print( "Exiting." );
		Platform.exit();
	}
}
