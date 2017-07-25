package eaglezr.infinitymirror.tests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class test extends Application {
	
	public static void main( String[] args ) {
		Application.launch( args );
	}

	@Override
	public void start( Stage arg0 ) throws Exception {
		Image image = new Image( "eaglezr\\infinitymirror\\resources\\images\\MirrorBase.png", true );
		ImageView imageView = new ImageView( image );	
		Pane pane = new Pane();
		pane.getChildren().add( imageView );
		Scene scene = new Scene(pane);
		arg0.setScene( scene );
		arg0.show();
	}
	
}
