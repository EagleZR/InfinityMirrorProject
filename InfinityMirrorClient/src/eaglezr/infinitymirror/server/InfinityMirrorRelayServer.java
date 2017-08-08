
package eaglezr.infinitymirror.server;

import eaglezr.infinitymirror.communications.ServerCommunicator;
import eaglezr.support.logs.LoggingTool;

public class InfinityMirrorRelayServer {
	
	// Server variables
	private static int port = 11896;

	static LoggingTool log = LoggingTool.getLogger();
	
	public static void main( String[] args ) {
		ServerCommunicator communicator = new ServerCommunicator( port );
	}
}
