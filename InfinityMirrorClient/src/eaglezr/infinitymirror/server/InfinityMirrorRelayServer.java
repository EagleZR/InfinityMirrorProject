
package eaglezr.infinitymirror.server;

import eaglezr.infinitymirror.communications.ServerCommunicator;
import eaglezr.support.LoggingTool;

public class InfinityMirrorRelayServer {
	
	// Server variables
	private static int port = 11896;
	
	static LoggingTool log = LoggingTool.startLogger( LoggingTool.Printers.CONSOLE_PRINTER, "im_server_log" );
	
	public static void main( String[] args ) {
		ServerCommunicator communicator = new ServerCommunicator( port );
	}
}
