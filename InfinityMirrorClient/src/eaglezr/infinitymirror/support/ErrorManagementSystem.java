
package eaglezr.infinitymirror.support;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Consumer;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ErrorManagementSystem implements Closeable {
	
	private static final int NUM_LOGS_TO_KEEP = 5;
	
	private static final String USER_TYPE_CLIENT = "client";
	private static final String USER_TYPE_SERVER = "server";
	private static final String USER_TYPE_APP = "app";
	
	public static enum Printers {
		LOG_PRINTER, CONSOLE_PRINTER, LABEL_PRINTER
	};
	
	public static enum UserTypes {
		CLIENT, SERVER, APP
	};
	
	private Label outputLabel;
	
	private UserTypes currType;
	
	Consumer<String> printer;
	
	Consumer<String> logPrinter;
	Consumer<String> consolePrinter;
	Consumer<String> labelPrinter;
	
	/**
	 * For use when there is no label
	 * 
	 * @param userType
	 */
	public ErrorManagementSystem( UserTypes userType, Printers defaultPrinter ) {
		new Thread( new ClearLogs() ).start();
		setDefaultPrinter( defaultPrinter );
		this.currType = userType;
		this.logPrinter = generateLogPrinter( userType );
		this.consolePrinter = generateConsolePrinter();
	}
	
	/**
	 * For use when there is a label
	 * 
	 * @param userType
	 * @param label
	 */
	public ErrorManagementSystem( UserTypes userType, Label label, Printers defaultPrinter ) {
		this( userType, defaultPrinter );
		this.outputLabel = label;
		this.labelPrinter = generateLabelPrinter( label );
	}
	
	/**
	 * Generates a printer that prints a String to the log file
	 * 
	 * @param userType
	 * @return
	 */
	public static Consumer<String> generateLogPrinter( UserTypes userType ) {
		GregorianCalendar calendar = new GregorianCalendar();
		File logFile = new File( "logs\\im_"
				+ ( userType == UserTypes.CLIENT ? USER_TYPE_CLIENT
						: ( userType == UserTypes.SERVER ? USER_TYPE_SERVER : USER_TYPE_APP ) )
				+ "_log " + calendar.get( GregorianCalendar.YEAR ) + "_" + calendar.get( GregorianCalendar.MONTH ) + "_"
				+ calendar.get( GregorianCalendar.DAY_OF_MONTH ) + "_" + calendar.get( GregorianCalendar.HOUR_OF_DAY )
				+ "_" + calendar.get( GregorianCalendar.MINUTE ) + "_" + calendar.get( GregorianCalendar.SECOND )
				+ ".txt" );
		Consumer<String> printer;
		try {
			logFile.getParentFile().mkdirs();
			logFile.createNewFile();
			@SuppressWarnings( "resource" )
			PrintStream outputStream = new PrintStream( logFile );
			printer = s -> {
				outputStream.println( new Date( System.currentTimeMillis() ).toString() + ": " + s );
			};
		} catch ( FileNotFoundException e ) {
			// TODO What should be done when errors have an error?
			printer = generateConsolePrinter();
			printer.accept(
					"There was an error while creating the logger...\nWhat do you do when not even the error handling system works?" );
			e.printStackTrace();
		} catch ( IOException e ) {
			printer = generateConsolePrinter();
			printer.accept( "The log file could not be created" );
			e.printStackTrace();
		}
		
		return printer;
	}
	
	/**
	 * Generates a printer that prints a String to the console
	 * 
	 * @return
	 */
	public static Consumer<String> generateConsolePrinter() {
		Consumer<String> printer = s -> {
			System.out.println( new Date( System.currentTimeMillis() ).toString() + ": " + s );
		};
		
		return printer;
	}
	
	/**
	 * Generates a printer printer that prints a String to a
	 * {@link javafx.scene.control.Label}
	 * 
	 * @param label
	 * @return
	 */
	public static Consumer<String> generateLabelPrinter( Label label ) {
		// FIXME Thread issue here
		Consumer<String> printer = s -> {
			label.setText( s );
		};
		return printer;
	}
	
	/**
	 * 
	 * @param printer
	 *            Use {@link ErrorManagementSystem.Printers}
	 */
	public void setDefaultPrinter( Printers printer ) {
		if ( printer == Printers.LABEL_PRINTER ) {
			this.printer = this.labelPrinter;
		} else if ( printer == Printers.CONSOLE_PRINTER ) {
			this.printer = this.consolePrinter;
		} else if ( printer == Printers.LOG_PRINTER ) {
			this.printer = this.logPrinter;
		} else {
			System.out.println( "Default printer could not be set" );
		}
	}
	
	/**
	 * Prints a String through the default printer
	 * 
	 * @param s
	 */
	public void print( String s ) {
		if ( this.printer != null ) {
			this.printer.accept( s );
		} else {
			printAll( "There is no set printer" );
		}
	}
	
	/**
	 * Prints a String through all of the printers
	 * 
	 * @param s
	 */
	public void printAll( String s ) {
		// FIXME Disabled for Thread issue
		// if (labelPrinter != null) {
		// labelPrinter.accept(s);
		// }
		if ( consolePrinter != null ) {
			consolePrinter.accept( s );
		}
		if ( logPrinter != null ) {
			logPrinter.accept( s );
		}
	}
	
	/**
	 * Displays an error over a {@link javafx.stage.Stage}
	 * 
	 * @param stage
	 * @param message
	 */
	public void displayError( Stage stage, String message ) {
		print( message );
		Label errorLabel = new Label( message );
		Button errorCloseButton = new Button( "Close" );
		
		BorderPane buttonPane = new BorderPane();
		buttonPane.setCenter( errorCloseButton );
		
		BorderPane errorPane = new BorderPane();
		errorPane.setCenter( errorLabel );
		errorPane.setBottom( buttonPane );
		
		Scene scene = new Scene( errorPane, message.length() * 9, 75 );
		
		Stage errorPopup = new Stage();
		errorCloseButton.setOnAction( new ErrorClose( errorPopup ) );
		errorPopup.setScene( scene );
		errorPopup.initModality( Modality.APPLICATION_MODAL );
		errorPopup.initOwner( stage );
		errorPopup.initStyle( StageStyle.UTILITY );
		errorPopup.resizableProperty().setValue( false );
		errorPopup.show();
	}
	
	public Label getLabel() {
		return this.outputLabel;
	}
	
	/**
	 * Manages the build-up of log files
	 */
	public void close() {
		new Thread(new ClearLogs()).start();
	}
	
	// Closes the error pop-up message
	private class ErrorClose implements EventHandler<ActionEvent> {
		Stage parent;
		
		public ErrorClose( Stage parent ) {
			this.parent = parent;
		}
		
		@Override
		public void handle( ActionEvent event ) {
			parent.close();
		}
	}
	
	@SuppressWarnings( "rawtypes" )
	private class ClearLogs extends Task {

		@Override
		protected Object call() throws Exception {
			purgeLogs();
			return null;
		}	
		
		private void purgeLogs() throws FileNotFoundException {
			File[] files = new File( "logs" ).listFiles();
			ArrayList<File> myFiles = new ArrayList<File>();
			ArrayList<File> filesToDelete = new ArrayList<File>();
			
			// Adds files to lists if they're empty or match this user type
			for ( int i = 0; i < files.length; i++ ) {
				files[i] = files[i].getAbsoluteFile();
				if ( files[i].length() == 0 ) {
					filesToDelete.add( files[i] );
				} else if ( files[i].getName()
						.contains( "im_"
								+ ( currType == UserTypes.CLIENT ? USER_TYPE_CLIENT
										: ( currType == UserTypes.SERVER ? USER_TYPE_SERVER : USER_TYPE_APP ) )
								+ "_log" ) ) {
					myFiles.add( files[i] );
				}
			}
			
			// Deletes empty files
			for ( int i = 0; i < filesToDelete.size(); i++ ) {
				
				// FIXME File.delete() isn't working
				if (!filesToDelete.get( i ).delete() ) {
					
				}
			}
			
			// Deletes the oldest files until only 5 are left
			while ( myFiles.size() > NUM_LOGS_TO_KEEP ) {
				File oldest = myFiles.get( 0 );
				for ( int i = 1; i < myFiles.size(); i++ ) {
					if ( getOlder( myFiles.get( i ).getName(), oldest.getName() ) ) {
						oldest = myFiles.get( i );
					}
				}
				oldest.delete();
			}
		}
		
		/**
		 * 
		 * @param left
		 * @param right
		 * @return true if {@linkleft} is older, false if {@linkright} is older
		 */
		private boolean getOlder( String left, String right ) {
			return left.compareTo( right ) > 0;
		}
		
		public void succeeded() {
			super.succeeded();
		}
		
		public void failed() {
			super.failed();
			print("Logs could not be cleared");
		}
	}
}