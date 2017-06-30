package eaglezr.infinitymirror.support;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ErrorManagementSystem implements Closeable {

	private static final String USER_TYPE_CLIENT = "client";
	private static final String USER_TYPE_SERVER = "server";
	private static final String USER_TYPE_APP = "app";
	
	public static enum Printers {LOG_PRINTER, CONSOLE_PRINTER, LABEL_PRINTER};
	public static enum UserTypes {CLIENT, SERVER, APP};
	
	private Label outputLabel;
	
	private UserTypes currType;
	
	Consumer<String> printer;
	
	Consumer<String> logPrinter;
	Consumer<String> consolePrinter;
	Consumer<String> labelPrinter;
	
	/**
	 * For use when there is no label
	 * @param userType
	 */
	public ErrorManagementSystem(UserTypes userType, Printers defaultPrinter) {
		setDefaultPrinter(defaultPrinter);
		this.currType = userType;
		this.logPrinter = generateLogPrinter(userType);
		this.consolePrinter = generateConsolePrinter();
	}
	
	/**
	 * For use when there is a label
	 * @param userType
	 * @param label
	 */
	public ErrorManagementSystem(UserTypes userType, Label label, Printers defaultPrinter) {
		this (userType, defaultPrinter);
		this.outputLabel = label;
		this.labelPrinter = generateLabelPrinter(label);
	}

	/**
	 * Generates a printer that prints a String to the log file
	 * @param userType
	 * @return
	 */
	public static Consumer<String> generateLogPrinter(UserTypes userType) {
		GregorianCalendar calendar = new GregorianCalendar();
		File logFile = new File("logs\\im_" + (userType == UserTypes.CLIENT ? USER_TYPE_CLIENT : (userType == UserTypes.SERVER ? USER_TYPE_SERVER : USER_TYPE_APP)) + "_log " + calendar.get(GregorianCalendar.YEAR) + "_"
				+ calendar.get(GregorianCalendar.MONTH) + "_" + calendar.get(GregorianCalendar.DAY_OF_MONTH) + "_"
				+ calendar.get(GregorianCalendar.HOUR_OF_DAY) + "_" + calendar.get(GregorianCalendar.MINUTE) + "_"
				+ calendar.get(GregorianCalendar.SECOND)+".txt");
		Consumer<String> printer;
		try {
			logFile.getParentFile().mkdirs();
			logFile.createNewFile();
			@SuppressWarnings("resource")
			PrintStream outputStream = new PrintStream(logFile);
			printer = s -> {
				outputStream.println(new Date(System.currentTimeMillis()).toString() + ": " + s);
			};
		} catch (FileNotFoundException e) {
			// TODO What should be done when errors have an error?
			printer = generateConsolePrinter();
			printer.accept(
					"There was an error while creating the logger...\nWhat do you do when not even the error handling system works?");
			e.printStackTrace();
		} catch (IOException e) {
			printer = generateConsolePrinter();
			printer.accept("The log file could not be created");
			e.printStackTrace();
		}

		return printer;
	}

	/**
	 * Generates a printer that prints a String to the console
	 * @return
	 */
	public static Consumer<String> generateConsolePrinter() {
		Consumer<String> printer = s -> {
			System.out.println(new Date(System.currentTimeMillis()).toString() + ": " + s);
		};

		return printer;
	}
	
	/**
	 * Generates a printer printer that prints a String to a {@link javafx.scene.control.Label}
	 * @param label
	 * @return
	 */
	public static Consumer<String> generateLabelPrinter(Label label) {
		// FIXME Thread issue here
		Consumer<String> printer = s -> {
			label.setText(s);
		};
		return printer;
	}
	
	/**
	 * 
	 * @param printer Use {@link ErrorManagementSystem.Printers}
	 */
	public void setDefaultPrinter(Printers printer) {
		if (printer == Printers.LABEL_PRINTER) {
			this.printer = this.labelPrinter;
		} else if (printer == Printers.CONSOLE_PRINTER) {
			this.printer = this.consolePrinter;
		} else if (printer == Printers.LOG_PRINTER) {
			this.printer = this.logPrinter;
		} else {
			System.out.println("Default printer could not be set");
		}
	}
	
	/**
	 * Prints a String through the default printer
	 * @param s
	 */
	public void print(String s) {
		if(this.printer != null) {
			this.printer.accept(s);
		} else {
			printAll("There is no set printer");
		}
	}
	
	/**
	 * Prints a String through all of the printers
	 * @param s
	 */
	public void printAll(String s) {
		// FIXME Disabled for Thread issue
//		if (labelPrinter != null) {
//			labelPrinter.accept(s);
//		}
		if (consolePrinter != null) {
			consolePrinter.accept(s);
		}
		if(logPrinter != null) {
			logPrinter.accept(s);
		}
	}

	/**
	 * Displays an error over a {@link javafx.stage.Stage}
	 * @param stage
	 * @param message
	 */
	public void displayError(Stage stage, String message) {
		print(message);
		Label errorLabel = new Label(message);
		Button errorCloseButton = new Button("Close");

		BorderPane buttonPane = new BorderPane();
		buttonPane.setCenter(errorCloseButton);

		BorderPane errorPane = new BorderPane();
		errorPane.setCenter(errorLabel);
		errorPane.setBottom(buttonPane);

		Scene scene = new Scene(errorPane, message.length() * 9, 75);

		Stage errorPopup = new Stage();
		errorCloseButton.setOnAction(new ErrorClose(errorPopup));
		errorPopup.setScene(scene);
		errorPopup.initModality(Modality.APPLICATION_MODAL);
		errorPopup.initOwner(stage);
		errorPopup.initStyle(StageStyle.UTILITY);
		errorPopup.resizableProperty().setValue(false);
		errorPopup.show();
	}
	
	
	public Label getLabel() {
		return this.outputLabel;
	}
	
	/**
	 * Manages the build-up of log files
	 */
	public void close() {
		print("Cleaning up log files");
		// FIXME Check for empty log files and delete them
		// 1. Cycle through each file
		// 	1a. Check if curr file is empty
		//  	1a(i). If empty, delete
		//  1b. Check if curr file includes currType's String name
		//	    1b(i). Count how many include that name
		// 2. If number of files remaining > 5, cycle through again
		// 	2a. Repeatedly delete the oldest file until only 5 remain 
	}

	// Closes the error pop-up message
	private class ErrorClose implements EventHandler<ActionEvent> {
		Stage parent;

		public ErrorClose(Stage parent) {
			this.parent = parent;
		}

		@Override
		public void handle(ActionEvent event) {
			parent.close();
		}
	}
}