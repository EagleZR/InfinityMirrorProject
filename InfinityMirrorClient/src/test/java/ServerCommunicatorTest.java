package eaglezr.infinitymirror.tests;

import eaglezr.infinitymirror.communications.ServerCommunicator;
import eaglezr.infinitymirror.support.InfinityMirror;
import eaglezr.support.logs.LoggingTool;
import javafx.scene.paint.Color;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This isn't gonna work... I think there's an issue with the threading, or something.
 */
public class ServerCommunicatorTest {

	static LoggingTool log = LoggingTool.getLogger();

	static ServerCommunicator communicator;
	static int port = 11896;

	@BeforeClass public static void setup() {
		log.addPrinter( LoggingTool.generateLogPrinter( "ServerCommunicatorTest" ) );
		communicator = new ServerCommunicator( port );
	}

	// Tests the UserVerification process.
	@Test public void testUserVerification() {

	}

	// Tests if there's any errors in listening for multiple connections.
	@Test public void testListeningMultipleConnections() {
		try {
			Socket socket1 = new Socket( Inet4Address.getLocalHost(), port );
			DataOutputStream out1 = new DataOutputStream( socket1.getOutputStream() );
			Socket socket2 = new Socket( Inet4Address.getLocalHost(), port );
			DataOutputStream out2 = new DataOutputStream( socket2.getOutputStream() );

			out1.writeInt( 1 );
			out2.writeInt( 1 );

			System.out.println( Inet4Address.getLocalHost() );
			System.out.println( communicator.getNumConnections() );

			assertTrue( communicator.getNumConnections() == 2 );

			socket1.close();
			socket2.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	// Tests if multiple clients are added to the server's list of known clients
	@Test public void testMultipleClientsAdded() {

	}

	// Tests the internal method for setting the mirror.
	@Test public void testGetSetMirrorMethod() {
		InfinityMirror mirror = new InfinityMirror( InfinityMirror.Mode.SOLID_COLOR_MODE, Color.RED );
		communicator.imWrapper.setCurrMirror( mirror );
		assertTrue( mirror.equals( communicator.imWrapper.getCurrMirror() ) );
	}

	// Tests the external system for getting the mirror.
	@Test public void testGetMirrorCommunication() {
		try {
			Socket socket1 = new Socket( Inet4Address.getLocalHost(), port );
			DataOutputStream out = new DataOutputStream( socket1.getOutputStream() );
			DataInputStream in = new DataInputStream( socket1.getInputStream() );
			InfinityMirror mirror1 = new InfinityMirror( InfinityMirror.Mode.SOLID_COLOR_MODE, Color.YELLOW );
			communicator.imWrapper.setCurrMirror( mirror1 );

			if ( in.readUTF().equals( "State purpose." ) ) {
				out.writeInt( 3 );

				InfinityMirror mirror2 = (InfinityMirror) new ObjectInputStream( in ).readObject();

				assertTrue( mirror1.equals( mirror2 ) );
			} else {
				System.out.println( "Misread connection error." );
				fail();
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
		}
	}

	// Tests the external system for setting the mirror.
	@Test public void testSetMirrorCommunication() {
		try {
			Socket socket1 = new Socket( Inet4Address.getLocalHost(), port );
			DataOutputStream out = new DataOutputStream( socket1.getOutputStream() );
			DataInputStream in = new DataInputStream( socket1.getInputStream() );

			if ( in.readUTF().equals( "State purpose." ) ) {
				out.writeInt( 2 );
				InfinityMirror mirror = new InfinityMirror( InfinityMirror.Mode.SOLID_COLOR_MODE, Color.BLUE );
				new ObjectOutputStream( out ).writeObject( mirror );
				assertTrue( mirror.equals( communicator.imWrapper.getCurrMirror() ) );
			} else {
				System.out.println( "Misread connection error." );
				fail();
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	@Test public void testSynchronization() {

	}

	// Tests the internal method for checking if the mirror has changed.
	@Test public void testMirrorChangedMethod() {

	}

	// Tests the external system for checking if the mirror has changed.
	@Test public void testMirrorChangedCommunication() {

	}
}
