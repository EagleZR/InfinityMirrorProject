package eaglezr.infinitymirror.communications;

import eaglezr.infinitymirror.support.InfinityMirror;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class ServerCommunicator extends Communicator {

	// TODO Way to communicate with thread?
	private ListeningThread listeningThread;
	private ArrayList<ConnectionThread> connections = new ArrayList<>();
	public InfinityMirrorWrapper imWrapper = new InfinityMirrorWrapper();

	/**
	 * Creates a {@link ServerCommunicator} that listens on a specified port.
	 *
	 * @param listeningPort - The port to listen for connections on.
	 */
	public ServerCommunicator( int listeningPort ) {
		super( listeningPort );
		System.out.println( "Making a new ServerCommunicator" );
		listeningThread = new ListeningThread( listeningPort );
	}

	private synchronized void startNewConnectionThread(ConnectionThread connection) {
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
	private class ListeningThread implements Runnable {

		private ServerSocket serverSocket;

		/**
		 * Creates a new listener at the specified port.
		 *
		 * @param port - The port used to listen for a new connection.
		 */
		public ListeningThread( int port ) {
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
	 * Handles new connections away from the {@link ListeningThread} so the listener can continue listening for new
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
						handleNewMirror( in, out );
					} else if (purpose == 3) {
						handleRetrieveMirror( in, out );
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
				// LATER Print "Connection cannot be closed" error
			}
		}

		/**
		 * When a connection is made, this method validates that the client is an authorized user.
		 *
		 * @param in
		 * @param out
		 */
		private boolean validateClient( DataInputStream in, DataOutputStream out ) {
			return true; // LATER Validate clients.
		}

		/**
		 * This method handles processes and communications necessary to handle the status request made by a
		 * connection.
		 *
		 * @param in
		 * @param out
		 */
		private void handleRequestStatus( DataInputStream in, DataOutputStream out ) {
			// TODO Handle Status Request
		}

		/**
		 * This method handles processes and communications for when a client wants to push a new mirror.
		 *
		 * @param in
		 * @param out
		 */
		private void handleNewMirror( DataInputStream in, DataOutputStream out ) {
			try {
				InfinityMirror newMirror = (InfinityMirror)new ObjectInputStream( in ).readObject();
				imWrapper.setCurrMirror( newMirror );
				out.writeBoolean( true );
			} catch ( IOException e ) {
				// TODO Print connection error
			} catch ( ClassNotFoundException e ) {
				// TODO Not sure what error to print
			}
		}

		/**
		 * This method handles processes and communications for when a client wants to pull the current mirror.
		 *
		 * @param in
		 * @param out
		 */
		private void handleRetrieveMirror( DataInputStream in, DataOutputStream out ) {
			// TODO Handle Retrieve Mirror Request
			try {
				new ObjectOutputStream (out).writeObject( imWrapper.getCurrMirror() );
			} catch ( IOException e ) {
				// TODO Print connection error
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
