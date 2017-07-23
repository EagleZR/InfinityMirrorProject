
package eaglezr.infinitymirror.support;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopupStage extends Stage {
	
	public PopupStage( Scene scene, Stage stage ) {
		this.setScene( scene );
		this.initModality( Modality.APPLICATION_MODAL );
		this.initOwner( stage );
		this.initStyle( StageStyle.UTILITY );
		this.resizableProperty().setValue( false );
	}
	
}
