package eaglezr.infinitymirror.communications;

import eaglezr.infinitymirror.support.InfinityMirror;
import eaglezr.support.logs.LoggingTool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class ServerCommunicator extends Communicator {

	// TODO Way to communicate with thread?
	private Listener listener;
	private ArrayList<ConnectionThread> connections = new ArrayList<>();
	public InfinityMirrorWrapper imWrapper = new InfinityMirrorWrapper();
	private LoggingTool log;

	/**
	 * Creates a {@link ServerCommunicator} that listens on a specified port.
	 *
	 * @param listeningPort - The port to listen for connections on.
	 */
	public ServerCommunicator( int listeningPort ) {
		super( listeningPort );
		log = LoggingTool.getLogger();
		System.out.println( "Making a new ServerCommunicator" );
		listener = new Listener( listeningPort );
		Thread listeningThread = new Thread (listener);
		listeningThread.setName( "Infinity Mirror Listen" );
	}

	private synchronized void startNewConnectionThread( ConnectionThread connection ) {
		// TODO Send connection to Executor or start running immediately?
		connections.add( connection );
	}

	/**
	 * Closes all connections.
	 */
	public void close() {
		// TODO Close the ServerCommunicator

	}

	/**
	 * Returns the number of current connections.
	 *
	 * @return
	 */
	public synchronized int getNumConnections() {
		return this.connections.size();
	}

	/**
	 * Class made to prevent locking issues when the current {@link InfinityMirror} is accessed.
	 */
	public class InfinityMirrorWrapper {
		private InfinityMirror currMirror;

		private boolean hasChanged;

		/**
		 * Checks if the client has the most recent {@link InfinityMirror} without needing to transmit the file. If the
		 * client is new and the information is not held, will return false;
		 *
		 * @return - Returns true if the client making the connection has not yet received the updated {@link
		 * InfinityMirror}.
		 */
		public synchronized boolean getHasChanged() {
			return this.hasChanged;
		}

		/**
		 * Sets the stored instance of the {@link InfinityMirror}.
		 *
		 * @param newMirror - The new mirror to be stored.
		 */
		public synchronized void setCurrMirror( InfinityMirror newMirror ) {
			this.hasChanged = true;
			this.currMirror = newMirror;
		}

		/**
		 * Retrieves the currently stored instance of the {@link InfinityMirror}.
		 *
		 * @return - The current {@link InfinityMirror}.
		 */
		public synchronized InfinityMirror getCurrMirror() {
			this.hasChanged = false;
			return this.currMirror;
		}
	}

	/**
	 * A {@link Runnable} instance that listens for a client to make a connection. Once made, the connection is passed
	 * to another thread so this thread can listen for more connections.
	 */
	private class Listener implements Runnable {

		private ServerSocket serverSocket;

		/**
		 * Creates a new listener at the specified port.
		 *
		 * @param port - The port used to listen for a new connection.
		 */
		public Listener( int port ) {
			try {
				serverSocket = new ServerSocket( port );
			} catch ( IOException e ) {
				// LATER Print "Unable to create socket" error.
			}
		}

		/**
		 * Waits for a connection and passes it to a new {@link ConnectionThread}.
		 */
		public void run() {
			while ( true ) {
				try {
					System.out.println( "Server is now waiting for a connection" );
					Socket connection = serverSocket.accept();
					System.out.println( "A connection has been made" );
					startNewConnectionThread( new ConnectionThread( connection ) );
				} catch ( IOException e ) {
					// LATER Print "Unable to form connection" error
				}
			}
		}

		/**
		 * Notify clients that a change has been made by another client.
		 */
		private void notifyClients() {
			// TODO Probably need to move this elsewhere...
		}
	}

	/**
	 * Handles new connections away from the {@link Listener} so the listener can continue listening for new
	 * connections while a current connection is handled.
	 */
	private class ConnectionThread implements Runnable {

		Socket connection;

		public ConnectionThread( Socket connection ) {
			// LATER Log connection
			this.connection = connection;
		}

		/**
		 * First validates the user, then determines the purpose of the connection, then passes the connection to the
		 * appropriate methods.
		 */
		public void run() {
			try {
				DataInputStream in = new DataInputStream( connection.getInputStream() );
				DataOutputStream out = new DataOutputStream( connection.getOutputStream() );
				if ( validateClient( in, out ) ) {
					out.writeUTF( "State purpose." ); // LATER Better way to do this?
					int purpose = in.readInt();
					if ( purpose == 1 ) {
						handleRequestStatus( in, out );
					} else if ( purpose == 2 ) {
						acceptMirror( in, out );
					} else if ( purpose == 3 ) {
						sendMirror( in, out );
					} else {
						// LATER Print "Connection purpose out of bounds" error
					}
				} else {
					out.writeChars( "Access denied." );
					// LATER Print unauthorized access attempted
				}
			} catch ( IOException e ) {
				// LATER Print "Unable to create stream" error
			}
			try {
				connection.close();
			} catch ( IOException e ) {
				// LATER Print "Connection closing error" error
			}
		}

		/**
		 * When a connection is made, this method validates that the client is an authorized user.
		 */
		private boolean validateClient( DataInputStream in, DataOutputStream out ) {
			log.print( "DON'T FORGET SECURITY!!!!!!!" );
			return true; // LATER Validate clients.
		}

		/**
		 * This method handles processes and communications necessary to handle the status request made by a
		 * connection.
		 */
		private void handleRequestStatus( DataInputStream in, DataOutputStream out ) {
			// TODO Handle Status Request
		}

		/**
		 * This method handles processes and communications for when a client wants to push a new mirror.
		 */
		private void acceptMirror( DataInputStream in, DataOutputStream out ) {
			try {
				InfinityMirror newMirror = (InfinityMirror) new ObjectInputStream( in ).readObject();
				imWrapper.setCurrMirror( newMirror );
				out.writeBoolean( true );
			} catch ( IOException e ) {
				log.print( "There was an error opening the ObjectInputStream." );
			} catch ( ClassNotFoundException e ) {
				log.print( "There was an error in recognizing the class while receiving a new mirror." );
			}
		}

		/**
		 * This method handles processes and communications for when a client wants to pull the current mirror.
		 */
		private void sendMirror( DataInputStream in, DataOutputStream out ) {
			// TODO Handle Retrieve Mirror Request
			try {
				boolean successful = false;
				while(!successful){
					new ObjectOutputStream( out ).writeObject( imWrapper.getCurrMirror() );
					successful = in.readBoolean();
					// LATER Read client response?
				}
			} catch ( IOException e ) {
				log.print( "There was an error opening the ObjectInputStream." );
			}
		}
	}

	/**
	 * Executor to handle the execution and termination of threads in the {@link ServerCommunicator}.
	 */
	private class ServerExecutor implements Executor {

		ArrayList<Thread> threads = new ArrayList<>();

		public synchronized void execute( Runnable runnable ) {

		}
	}
}
