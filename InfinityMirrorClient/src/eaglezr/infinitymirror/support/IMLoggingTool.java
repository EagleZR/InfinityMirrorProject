
package eaglezr.infinitymirror.support;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Consumer;
import eaglezr.support.LoggingTool;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

// TODO Make an eaglezr.infinitymirror.support.IMLoggingTool that extends
// eaglezr.support.LoggingTool with the IM-specific stuffs?
public class IMLoggingTool extends LoggingTool {
	
	public static enum Printers {
		LOG_PRINTER( "log" ), CONSOLE_PRINTER( "console" ), LABEL_PRINTER( "label" );
		
		public final String NAME;
		
		private Printers( String name ) {
			this.NAME = name;
		}
	};
	
	public static enum UserTypes {
		CLIENT( "client" ), SERVER( "server" ), APP( "app" );
		
		public final String NAME;
		
		private UserTypes( String name ) {
			this.NAME = name;
		}
	};
	
	private Label outputLabel;
	
	protected Consumer<String> defaultPrinter;
	
	Consumer<String> labelPrinter;
	
	/**
	 * For use when there is a label
	 * 
	 * @param userType
	 * @param label
	 */
	public static LoggingTool startLogger( UserTypes userType, Label label, Printers defaultPrinter ) {
		logger = new IMLoggingTool( userType, label, defaultPrinter );
		return logger;
	}
	
	private IMLoggingTool( UserTypes userType, Label label, Printers defaultPrinter ) {
		super( new File( "logs\\im_" + userType.NAME + "_log " + new GregorianCalendar().get( GregorianCalendar.YEAR )
				+ "_" + new GregorianCalendar().get( GregorianCalendar.MONTH ) + "_"
				+ new GregorianCalendar().get( GregorianCalendar.DAY_OF_MONTH ) + "_"
				+ new GregorianCalendar().get( GregorianCalendar.HOUR_OF_DAY ) + "_"
				+ new GregorianCalendar().get( GregorianCalendar.MINUTE ) + "_"
				+ new GregorianCalendar().get( GregorianCalendar.SECOND ) + ".txt" ) );
		this.outputLabel = label;
		this.labelPrinter = generateLabelPrinter( label );
		setDefaultPrinter( defaultPrinter );
	}
	
	public static IMLoggingTool getLogger() {
		if ( logger != null && logger.getClass() == IMLoggingTool.class ) {
			return (IMLoggingTool) logger;
		} else {
			logger.printAll( Error.IM_LOG_NULL.getText() );
			return null;
		}
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
			this.defaultPrinter = this.labelPrinter;
		} else {
			super.setDefaultPrinter( LoggingTool.Printers.valueOf( printer.NAME ) );
		}
	}
	
	/**
	 * Prints a String through all of the printers
	 * 
	 * @param s
	 */
	public void printAll( String s ) {
		// FIXME Make Printer work with Platform.runLater();
//		 if (labelPrinter != null) {
//		 Platform.runLater( labelPrinter.accept(s) );
//		 }
		super.printAll( s );
	}
	
	public Label getLabel() {
		return this.outputLabel;
	}
}
