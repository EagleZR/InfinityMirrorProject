package eaglezr.infinitymirror.communications;
import java.security.AccessControlException;

public abstract class Communicator {
	
	protected String url;
	protected int port;
	
	protected Communicator(String url, int port) {
		this.url = url;
		this.port = port;
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
