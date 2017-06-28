package eaglezr.infinitymirror.communications;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.AccessControlException;

import javafx.concurrent.Task;

public class ClientCommunicator extends Communicator {
	
	// FIXME Make Constructor
	public ClientCommunicator(String url, int port) {
		super(url, port);
	}
	
	private String sendMessage(int command) {
		try {
			SendMessageTask sendMessage = new SendMessageTask(command);
			Thread thread = new Thread(sendMessage);

			thread.setDaemon(false);
			thread.start();
			return "";
		} catch (Exception e) {
			return "E1: Connection not made";
		}
	}

	// FIXME Move the Task back to the client
	private class SendMessageTask extends Task {

		private int command;
		private int response;

		public SendMessageTask(int command) {
			this.command = command;
		}

		@Override
		protected Object call() throws Exception {
			response = -1;
			try { // Any
				Socket socket = new Socket(url, port);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				formConnection();
				out.write(command);
				response = in.read();
				socket.close();
			} catch (IOException e) {
				return "E1: Connection not made";
			} catch (AccessControlException e) {
				return "E5: Connection Rejected";
			}

			// FIXME Determine if possible to avoid returning anything
			return response;
		}

		@Override
		protected void succeeded() {
			super.succeeded();
//			parseServerResponse(command, response);
		}

		@Override
		protected void failed() {
			super.failed();
//			ems.displayError(primaryStage, "E4: Communications Thread Failed");
		}
	}
}
