package eaglezr.infinitymirror.communications;

import java.io.*;
import java.net.Socket;
import java.security.AccessControlException;

import eaglezr.infinitymirror.support.ErrorPopupSystem;
import eaglezr.infinitymirror.support.IMLoggingTool;
import eaglezr.infinitymirror.support.InfinityMirror;
import eaglezr.infinitymirror.support.Error;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ClientCommunicator extends Communicator {

	private static ListenerService listener;
	private IMLoggingTool log;

	public ClientCommunicator( String url, int port ) {
		super( url, port );
		this.log = IMLoggingTool.getLogger();
		if ( listener == null ) {
			listener = new ListenerService( url, port );
		}
	}

	public void pushMirror( InfinityMirror mirror ) {
		Thread sendMessageThread = new Thread( new SendMirrorTask( mirror ) );
		sendMessageThread.setName( "Infinity Mirror Client Push" );
		sendMessageThread.start();
	}


	public void close() {
		// TODO Close ClientCommunicator
	}

	private class SendMirrorTask extends Task {

		private InfinityMirror mirror;
		private int response;
		private Error error = Error.DEFAULT;

		SendMirrorTask( InfinityMirror mirror ) {
			this.mirror = mirror;
		}

		@Override protected Object call() throws Exception {
			response = -1;
			try {
				log.print( "Pushing mirror." );
				Socket socket = new Socket( url, port );
				DataInputStream in = new DataInputStream( socket.getInputStream() );
				DataOutputStream out = new DataOutputStream( socket.getOutputStream() );
				if (connectToServer( in, out )) {
					ObjectOutputStream o_out = new ObjectOutputStream( out );
					formConnection();
					o_out.writeObject( mirror );
					response = in.read();
					if (response == 1) { // LATER Establish server response
						IMLoggingTool.print( "Mirror successfully pushed." );
					} else {
						IMLoggingTool.print( "Mirror could not be pushed." );
					}
				} else {
					// LATER EMS: "Unable to authenticate with server."
				}
				socket.close();
			} catch ( IOException e ) {
				error = Error.CONNECTION_NOT_MADE;
			} catch ( AccessControlException e ) {
				error = Error.CONNECTION_REJECTED;
			}

			// LATER Determine if possible to avoid returning anything
			return response;
		}

		private boolean connectToServer(DataInputStream in, DataOutputStream out) {
			// LATER Client: Connect to server
			log.print( "DON'T FORGET SECURITY!!!!!!!!" );
			return true;
		}

		@Override protected void succeeded() {
			super.succeeded();
			log.print( "Push Mirror Thread succeeded. Closing Thread." );
			if ( error != Error.DEFAULT ) {
				ErrorPopupSystem.displayError( error );
			}
		}

		@Override protected void failed() {
			super.failed();
			log.print( "Push Mirror Thread failed. Closing Thread." );
			if ( error != Error.DEFAULT ) {
				ErrorPopupSystem.displayError( error );
			}
		}
	}

	/**
	 * Creates a server on the client that is constantly listening to the remote server to detect changes. The process
	 * is separated onto another thread so its running doesn't noticeably affect the running of the client. A listening
	 * service was used so the client isn't constantly calling the server for updates.
	 *
	 * @author Mark
	 */
	@SuppressWarnings( "rawtypes" ) private class ListenerService extends Service implements Closeable {

		private final String url;
		private final int port;
		private InfinityMirror currMirror;

		ListenerService( String url, int port ) {
			this.url = url;
			this.port = port;
		}

		@Override protected Task createTask() {
			return new ServerListenerTask();
		}

		public synchronized InfinityMirror getMirror() {
			return this.currMirror;
		}

		public synchronized void setMirror( InfinityMirror newMirror ) {
			this.currMirror = newMirror;
		}

		public void close() {

		}

		/**
		 * The Task will wait to hear back from the server. Once it hears from the server, it will update the main
		 * thread from the Task.succeeded() method.
		 *
		 * @author Mark
		 */
		private class ServerListenerTask extends Task {

			private final String url;
			private final int port;

			//////////////
			// Main thread
			//////////////


			public ServerListenerTask() {
				this.url = ListenerService.this.url;
				this.port = ListenerService.this.port;
			}

			// Update ClientController from here
			protected void succeeded() {
				super.succeeded();
				// TODO Update according to server response
			}

			protected void failed() {
				super.failed();
				// TODO Make Error for log.print( "Could not update from the server" );
			}

			//////////////////
			// Separate thread
			//////////////////


			@Override protected Object call() throws Exception {
				// FIXME Listen for a signal from the server

				return null;
			}
		}
	}
}
