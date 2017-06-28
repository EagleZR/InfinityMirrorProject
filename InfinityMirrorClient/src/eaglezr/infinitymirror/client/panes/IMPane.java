package eaglezr.infinitymirror.client.panes;

import eaglezr.infinitymirror.client.ClientController;
import eaglezr.infinitymirror.support.ClientCommands;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

abstract class IMPane extends Pane {

	/**
	 * Add all content to be displayed in here.
	 */
	protected Pane container = new Pane();
	public Label outputLabel = new Label("");
	public MyMenuBar menu;

	protected IMPane(ClientController controller) {
		
		BorderPane shell = buildPaneShell();
		this.getChildren().add(shell);
	}

	private BorderPane buildPaneShell() {
		// Build Application Pane
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(this.menu = new MyMenuBar());
		BorderPane innerPane = new BorderPane();
		innerPane.setTop(buildInfinityMirrorPane());
		innerPane.setCenter(this.container);
		mainPane.setCenter(innerPane);
		mainPane.setBottom(outputLabel);
		return mainPane;
	}
	
	private Pane buildInfinityMirrorPane() {
		BorderPane pane = new BorderPane();
		Image image = new Image("MirrorBase.png");
		ImageView imageView = new ImageView(image);
		pane.setCenter(imageView);
		EventHandler<ActionEvent> eventHandler = e -> {
			// offsetColor();
			// TODO colorLights(paneType);
		};

		Timeline changingLights = new Timeline(new KeyFrame(Duration.millis(250), eventHandler));

		return pane;
	}

	private class MyMenuBar extends MenuBar {
		
		public MenuItem returnToMain;
		public MenuItem menuWhiteLightMode;
		public MenuItem menuTurnOnLights;
		public MenuItem menuTurnOffLights;
		public MenuItem menuExit;
		
		public MyMenuBar() {
			// Create Menus
			Menu fileMenu = new Menu("File");

			returnToMain = new MenuItem("Main Menu");
//			returnToMain.setOnAction(this.displayMainMenu);

			menuWhiteLightMode = new MenuItem("Toggle White Light");
//			menuWhiteLightMode.setOnAction(toggleWhiteLight);

			menuTurnOnLights = new MenuItem("Turn on lights");
//			menuTurnOnLights.setOnAction(toggleLights);
			menuTurnOnLights.setVisible(false);

			menuTurnOffLights = new MenuItem("Turn off lights");
//			menuTurnOffLights.setOnAction(toggleLights);

			menuExit = new MenuItem("Exit");
			fileMenu.getItems().addAll(returnToMain, menuWhiteLightMode, menuTurnOnLights, menuTurnOffLights, menuExit);

			Menu optionsMenu = new Menu("Options");
			// TODO Add Options Menu components

			Menu helpMenu = new Menu("Help");
			// TODO Add Help Menu components

			// Create Menu Bar
			this.getMenus().addAll(fileMenu, optionsMenu, helpMenu);
		}
		
	}
	
}
