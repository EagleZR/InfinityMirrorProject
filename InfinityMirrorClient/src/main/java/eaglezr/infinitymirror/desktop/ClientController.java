package eaglezr.infinitymirror.desktop;

import eaglezr.infinitymirror.communications.ClientCommunicator;
import eaglezr.infinitymirror.support.IMLoggingTool;
import eaglezr.infinitymirror.support.InfinityMirror;
import eaglezr.support.logs.LoggingTool;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.security.NoSuchAlgorithmException;

public class ClientController {

	private InfinityMirror currMirror = new InfinityMirror( InfinityMirror.Mode.SOLID_COLOR_MODE, Color.RED );
	private ClientCommunicator communicator;
	private LoggingTool loggingTool;

	public ClientController( String url, int port, LoggingTool loggingTool ) throws NoSuchAlgorithmException {
		this.loggingTool = loggingTool;
		this.loggingTool.print( "Initializing new InfinityMirror Desktop Client." );
		this.loggingTool.print( "Initializing new ClientCommunicator." );
		communicator = new ClientCommunicator( url, port );
	}

	public void pushMirror( InfinityMirror mirror ) {
		communicator.pushMirror( mirror );
	}

	public void exit() {
		IMLoggingTool.print( "Exiting." );
		Platform.exit();
	}
}
