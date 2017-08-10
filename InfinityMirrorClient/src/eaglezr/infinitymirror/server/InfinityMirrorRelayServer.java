
package eaglezr.infinitymirror.server;

import eaglezr.infinitymirror.communications.ServerCommunicator;
import eaglezr.support.logs.LoggingTool;

public class InfinityMirrorRelayServer {
	
	// Server variables
	private static int port = 11896;

	static LoggingTool log = LoggingTool.getLogger();
	
	public static void main( String[] args ) {
		log.addAllPrinters( LoggingTool.generateLogPrinter( "im_server" ) );
//		log.setDefault( log.getPrinters().get( 1 ) );
		ServerCommunicator communicator = new ServerCommunicator( port );
		while(true) {
			// TODO Check if communicator is still running. If not, start it up again
		}
	}
}
