import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class test extends Application {
	
	public static void main( String[] args ) {
		// TODO Auto-generated method stub
		Application.launch( args );
	}

	@Override
	public void start( Stage arg0 ) throws Exception {
		GridPane pane = new GridPane();
		pane.add( new Button(), 0, 0 );
		pane.add( new Button(), 1, 0 );		
	}
	
}
