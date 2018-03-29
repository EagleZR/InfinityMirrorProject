package eaglezr.javafx.panes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class ButtonPaneExampleApplication extends Application {

	public static void main( String[] args ) {
		launch( args );
	}

	@Override public void start( Stage primaryStage ) {

		BorderPane pane = new BorderPane();
		ButtonPane buttonPane = new ButtonPane();

		Button addButton = new Button( "Add Button" );
		Button removeButton = new Button( "Remove Button" );
		Button flip = new Button( "flip" );
		Button addMaxY = new Button( "Increase Max Y" );
		Button decMaxY = new Button( "Decrease Max Y" );
		Button addMinX = new Button( "Increase Min X" );
		Button decMinX = new Button( "Decrease Min X" );

		addButton.setOnAction( event -> {
			buttonPane.addButton( new Button( "Button" ) );
		} );
		removeButton.setOnAction( event -> {
			buttonPane.removeButton( buttonPane.getButtons().length > 0 ? buttonPane.getButtons()[0] : null );
		} );
		flip.setOnAction( event -> {
			buttonPane.setOrientation( ( buttonPane.getOrientation() == ButtonPane.Orientation.Horizontal ?
					ButtonPane.Orientation.Vertical :
					ButtonPane.Orientation.Horizontal ) );
			primaryStage.setTitle( buttonPane.orientation.name() );
		} );
		addMaxY.setOnAction( event -> {
			buttonPane.setMaxYExpansion( buttonPane.getMaxYExpansion() + 1 );
			addMaxY.setText( "Increase Max Y: " + buttonPane.getMaxYExpansion() );
			decMaxY.setText( "Decrease Max Y: " + buttonPane.getMaxYExpansion() );
		} );
		decMaxY.setOnAction( event -> {
			buttonPane.setMaxYExpansion( buttonPane.getMaxYExpansion() - 1 );
			addMaxY.setText( "Increase Max Y: " + buttonPane.getMaxYExpansion() );
			decMaxY.setText( "Decrease Max Y: " + buttonPane.getMaxYExpansion() );
		} );
		addMinX.setOnAction( event -> {
			buttonPane.setMinXRetraction( buttonPane.getMinXRetraction() + 1 );
			addMinX.setText( "Increase Min X: " + buttonPane.getMinXRetraction() );
			decMinX.setText( "Decrease Min X: " + buttonPane.getMinXRetraction() );
		} );
		decMinX.setOnAction( event -> {
			buttonPane.setMinXRetraction( buttonPane.getMinXRetraction() - 1 );
			addMinX.setText( "Increase Min X: " + buttonPane.getMinXRetraction() );
			decMinX.setText( "Decrease Min X: " + buttonPane.getMinXRetraction() );
		} );

		pane.setLeft( addButton );
		pane.setRight( removeButton );
		pane.setCenter( buttonPane );
		HBox hBox = new HBox();
		hBox.getChildren().addAll( addMaxY, addMinX, flip, decMinX, decMaxY );
		pane.setBottom( hBox );

		Scene scene = new Scene( pane );
		primaryStage.setScene( scene );
		primaryStage.show();
	}
}
