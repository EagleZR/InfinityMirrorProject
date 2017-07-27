package eaglezr.infinitymirror.communications;
import java.io.Closeable;
import java.security.AccessControlException;

public abstract class Communicator implements Closeable {

	public enum CommunicatorType {
		CLIENT,
		SERVER;
	}

	protected String url;
	protected int port;
	protected CommunicatorType type;
	
	protected Communicator(String serverURL, int outputPort) {
		this.type = CommunicatorType.CLIENT;
		this.url = serverURL;
		this.port = outputPort;
	}

	protected Communicator(int listeningPort) {
		this.type = CommunicatorType.SERVER;
		this.port = listeningPort;
	}
	private void transmit(Byte[] message) {
		// FIXME Add security system
	} 

	private Byte[] receive(int request, int buffer) {
		Byte[] received = new Byte[buffer];
		// FIXME Add security system
		return received;
	}
	
	protected void formConnection() throws AccessControlException {
		// FIXME Add security system
	}
}
