
package eaglezr.infinitymirror.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class InfinityMirrorDesktopClient extends Application {
	
	private int port = 11896;
	private String url = "136.59.71.218";
	protected ClientController controller;
	
	/**
	 * Included for Eclipse
	 * 
	 * @param args
	 */
	public static void main( String[] args ) {
		Application.launch( args );
	}
	
	@Override
	public void start( Stage stage ) throws Exception {
		
		this.controller = new ClientController( stage );
	}

}
