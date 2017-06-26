import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InfinityMirrorRelayServer {
	
	// Server variables
	private static int port = 11896;
	private static DataInputStream in;
	private static DataOutputStream out;
	private static ServerSocket serverSocket;
	
	// Lamp Status Variables 
	private static boolean lightsOn = true;
	private static boolean whiteLightMode = false;
	
	public static void main( String[] args ) {
		try { // If this breaks, we're SOL, and the program will need to restart
			serverSocket = new ServerSocket( port );
			// File log = new File("log" + )
			while ( true ) {
				try {
					Socket connection = serverSocket.accept();
					in = new DataInputStream( connection.getInputStream() );
					out = new DataOutputStream( connection.getOutputStream() );
					try { // Assuming misread input
						int input = in.read();
						System.out.println( "Read: " + input );
						int output = -1;
						if ( input == ClientCommands.LIGHTS.COMMAND ) { // Toggle on/off
							lightsOn = !lightsOn;
							// TODO write to file
							output = ( lightsOn ? 11 : 10 );
						} else if ( input == 2 ) { // Toggle white light
							whiteLightMode = !whiteLightMode;
							// TODO write to file
							output = ( whiteLightMode ? 21 : 20 );
						} else if ( input == 3 ) { // Solid Color Mode
							output = 3; // echo command
							// TODO figure out how to read a color
							// TODO set color to primary color, and change mode
						} else if ( input == 4 ) { // Desktop Harmony Mode
							// TODO
						} else if ( input == 5 ) { // Sound Responsive Mode
							// TODO
						} else if ( input == 6 ) { // Music Responsive Mode
							// TODO
						} else { // Input not recognized
							out.writeInt( -1 ); // error response
						}
						System.out.println( output );
						out.write( output );
					} catch ( IOException e ) {
						out.writeInt( -1 ); // error response
						System.out.println ( "There was an error sending the message" );
					}
					connection.close();
				} catch ( IOException e ) { // Trouble opening the connection
					System.out.println( "Connection cannot be made." );
				}
			}
		} catch ( IOException e ) {
			System.out.println( "The socket could not be opened." );
		} finally {
			try {
				serverSocket.close();
			} catch ( IOException e ) {
				System.out.println( "The socket could not be closed." );
			}
		}
	}
}