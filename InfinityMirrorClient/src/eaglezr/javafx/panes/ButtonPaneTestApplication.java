package eaglezr.javafx.panes;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.Test;

import static javafx.application.Application.launch;
import static org.junit.Assert.*;

public class ButtonPaneTestApplication extends Application {

	public static void main( String[] args ) {
		launch( args );
	}

	@Override public void start( Stage primaryStage ) {
		try {
			getPrefMaxHeight();
			setPrefMaxHeight();
			getMinButtonWidth();
			setMinWidth();
			addButton();
			addButtons();
			removeButton();
			removeButtons();
			getButtons();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test public void getPrefMaxHeight() throws Exception {
		ButtonPane buttonPane = new ButtonPane( new Button("Button 1") );
		assertTrue( buttonPane.prefMaxHeight == buttonPane.getPrefMaxHeight() );

		buttonPane = new ButtonPane( 1, 4, new Button( "Hello" ) );
		assertTrue( buttonPane.prefMaxHeight == 4 );
	}

	@Test public void setPrefMaxHeight() throws Exception {
		ButtonPane buttonPane = new ButtonPane( new Button( "Hi" ) );
		buttonPane.setPrefMaxHeight( 8 );
		assertTrue( buttonPane.getPrefMaxHeight() == 8 );
	}

	@Test public void getMinButtonWidth() throws Exception {
		ButtonPane buttonPane = new ButtonPane( new Button("Button 1") );
		assertTrue( buttonPane.minWidth == buttonPane.getMinButtonWidth() );

		buttonPane = new ButtonPane( 1, 4, new Button( "Hello" ) );
		assertTrue( buttonPane.getMinButtonWidth() == 1 );
	}

	@Test public void setMinWidth() throws Exception {
		ButtonPane buttonPane = new ButtonPane( new Button( "Hi" ) );
		buttonPane.setMinWidth( 15 );
		assertTrue( buttonPane.getMinButtonWidth() == 15 );
	}

	@Test public void addButton() throws Exception {
		ButtonPane buttonPane = new ButtonPane( new Button( "Button 1" ) );
		buttonPane.addButton( new Button( "Button 2" ) );
		assertTrue( buttonPane.buttons.length == 2 );

		buttonPane.addButton( null );
		assertTrue( buttonPane.buttons.length == 2 );
	}

	@Test public void addButtons() throws Exception {
		ButtonPane buttonPane = new ButtonPane( new Button( "Button 1" ) );
		buttonPane.addButtons( new Button( "Button 2" ) );
		assertTrue( buttonPane.buttons.length == 2 );

		buttonPane.addButtons( new Button( "Button 3" ), new Button( "Button 4" ) );
		assertTrue( buttonPane.buttons.length == 4 );

		buttonPane.addButtons( null );
		assertTrue( buttonPane.buttons.length == 4 );
	}

	@Test public void removeButton() throws Exception {
		Button button1 = new Button( "Button 1" );
		Button button2 = new Button( "Button 2" );
		ButtonPane buttonPane = new ButtonPane( button1, button2 );

		buttonPane.removeButton( button1 );
		assertTrue( buttonPane.buttons.length == 1 );

		buttonPane.removeButton( button1 );
		assertTrue( buttonPane.buttons.length == 1 );

		buttonPane.removeButton( null );
		assertTrue( buttonPane.buttons.length == 1 );
	}

	@Test public void removeButtons() throws Exception {
		Button button1 = new Button( "Button 1" );
		Button button2 = new Button( "Button 2" );
		Button button3 = new Button( "Button 3" );
		Button button4 = new Button( "Button 4" );
		ButtonPane buttonPane = new ButtonPane( button1, button2, button3, button4 );

		buttonPane.removeButtons( button1 );
		assertTrue( buttonPane.buttons.length == 3 );

		buttonPane.removeButtons( button3, button4 );
		assertTrue( buttonPane.buttons.length == 1 );

		buttonPane.removeButtons( button1, button3, button4 );
		assertTrue( buttonPane.buttons.length == 1 );

		buttonPane.removeButtons( null );
		assertTrue( buttonPane.buttons.length == 1 );
	}

	@Test public void getButtons() throws Exception {
		Button[] buttons = { new Button("Button 1"), new Button("Button 2"), new Button("Button 3")};
		ButtonPane buttonPane = new ButtonPane( buttons );
		assertTrue( buttonPane.getButtons().equals( buttons ) );
	}
}
