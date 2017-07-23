
package eaglezr.infinitymirror.communications;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.AccessControlException;
import eaglezr.infinitymirror.support.Error;
import eaglezr.infinitymirror.support.ErrorManagementSystem;
import eaglezr.infinitymirror.support.InfinityMirror;
import eaglezr.infinitymirror.support.IMLoggingTool;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ClientCommunicator extends Communicator {
	
	private static ListenerService listener;
	private IMLoggingTool log;
	private ErrorManagementSystem ems;
	
	public ClientCommunicator( String url, int port, IMLoggingTool log, ErrorManagementSystem ems ) {
		super( url, port );
		this.log = log;
		this.ems = ems;
		if ( listener == null ) {
			listener = new ListenerService();
		}
	}
	
//	private void sendMessage( int command ) {
//		try {
//			SendMessageTask sendMessage = new SendMessageTask( command );
//			Thread thread = new Thread( sendMessage );
//			
//			thread.setDaemon( false );
//			thread.start();
//		} catch ( Exception e ) {
//			log.print( Error.COMMAND_NOT_SENT.toString() );
//		}
//	}
	
	public void pushMirror( InfinityMirror mirror ) {
		
	}
	
	public boolean checkMirrorChanged() {
		
		return false;
	}
	
	// // How do I want to do this?
	// // Create IM object and update everything from that?
	// private Byte[] getCache() {
	//
	// return null;
	// }
	
	private class SendMessageTask extends Task {
		
		private int command;
		private int response;
		
		public SendMessageTask( int command ) {
			this.command = command;
		}
		
		@Override
		protected Object call() throws Exception {
			response = -1;
			try { // Any
				Socket socket = new Socket( url, port );
				DataInputStream in = new DataInputStream( socket.getInputStream() );
				DataOutputStream out = new DataOutputStream( socket.getOutputStream() );
				formConnection();
				out.write( command );
				response = in.read();
				socket.close();
			} catch ( IOException e ) {
				return "E1: Connection not made";
			} catch ( AccessControlException e ) {
				return "E5: Connection Rejected";
			}
			
			// FIXME Determine if possible to avoid returning anything
			return response;
		}
		
		@Override
		protected void succeeded() {
			super.succeeded();
			// parseServerResponse(command, response);
		}
		
		@Override
		protected void failed() {
			super.failed();
			// ems.displayError(primaryStage, "E4: Communications Thread
			// Failed");
		}
	}
	
	/**
	 * Creates a server on the client that is constantly listening to the remote
	 * server to detect changes. The process is separated onto another thread so
	 * its running doesn't noticeably affect the running of the client. A
	 * listening service was used so the client isn't constantly calling the
	 * server for updates.
	 * 
	 * @author Mark
	 *
	 */
	@SuppressWarnings( "rawtypes" )
	private class ListenerService extends Service {
		
		private final String url;
		private final int port;
		
		public ListenerService() {
			this.url = ClientCommunicator.this.url;
			this.port = ClientCommunicator.this.port;
		}
		
		@Override
		protected Task createTask() {
			return new ServerListenerTask();
		}

		/**
		 * The Task will wait to hear back from the server. Once it hears from
		 * the server, it will update the main thread from the
		 * {@link succeeded()} method.
		 * 
		 * @author Mark
		 *
		 */
		private class ServerListenerTask extends Task {
			
			private final String url;
			private final int port;
			
			// Main thread
			public ServerListenerTask() {
				this.url = ListenerService.this.url;
				this.port = ListenerService.this.port;
			}
			
			// Separate thread
			@Override
			protected Object call() throws Exception {
				// FIXME Listen for a signal from the server
				
				return null;
			}
			
			// Main thread
			/**
			 * Update data from here
			 */
			protected void succeeded() {
				super.succeeded();
				// TODO Update according to server response
			}
			
			// Main thread
			protected void failed() {
				super.failed();
				log.print( "Could not update from the server" );
			}
		}
	}
}
