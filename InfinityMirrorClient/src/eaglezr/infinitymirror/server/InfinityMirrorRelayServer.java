
package eaglezr.infinitymirror.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import eaglezr.infinitymirror.support.ClientCommands;
import eaglezr.support.LoggingTool;

public class InfinityMirrorRelayServer {
	
	// Server variables
	private static int port = 11896;
	private static DataInputStream in;
	private static DataOutputStream out;
	private static ServerSocket serverSocket;
	
	// Lamp Status Variables
	private static boolean lightsOn = true;
	private static boolean whiteLightMode = false;
	
	static LoggingTool log = LoggingTool.startLogger( LoggingTool.Printers.CONSOLE_PRINTER, "im_server_log" );
	
	public static void main( String[] args ) {
		
		try { // If this breaks, we're SOL, and the program will need to restart
			serverSocket = new ServerSocket( port );
			// File log = new File("log" + )
			while ( true ) {
				try {
					Socket connection = serverSocket.accept();
					in = new DataInputStream( connection.getInputStream() );
					out = new DataOutputStream( connection.getOutputStream() );
					try { 
						int input = in.read();
						log.print( "Read: " + input );
						int output = -1;
						if ( input == ClientCommands.LIGHTS.COMMAND ) { // Toggle
																		// on/off
							lightsOn = !lightsOn;
							// TODO Communicate with controller?
							output = ( lightsOn ? 11 : 10 );
						} else if ( input == 2 ) { // Toggle white light
							whiteLightMode = !whiteLightMode;
							// TODO Communicate with controller?
							output = ( whiteLightMode ? 21 : 20 );
						} else if ( input == 3 ) { // Solid Color Mode
							output = 3; // echo command
							// TODO Figure out how to read a color
							// TODO Set color to primary color, and change mode
						} else if ( input == 4 ) { // Alternating Color Mode
							// TODO Handle command
						} else if ( input == 5 ) { // Rainbow Mode
							// TODO Handle Rainbow Mode command
						} else if ( input == 6 ) { // Rainbow Pulse Mode
							// TODO Handle Rainbow Pulse Mode command
						} else if ( input == 7 ) {
							// TODO Handle Pulse Mode command
						} else { // Input not recognized
							out.writeInt( -1 ); // error response
						}
						log.print( "Returned: " + output );
						out.write( output );
					} catch ( IOException e ) {
						out.writeInt( -1 ); // error response
						log.print( "There was an error sending the message" );
					}
					connection.close();
				} catch ( IOException e ) { // Trouble opening the connection
					log.print( "Connection cannot be made." );
				}
			}
		} catch ( IOException e ) {
			log.print( "The socket could not be opened." );
		} finally {
			try {
				serverSocket.close();
			} catch ( IOException e ) {
				log.print( "The socket could not be closed." );
			}
		}
	}
}
