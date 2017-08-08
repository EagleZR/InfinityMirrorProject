package eaglezr.infinitymirror.support;

import java.util.function.Consumer;

import eaglezr.support.logs.LoggingTool;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * An extension of {@link LoggingTool} specifically made for {@link eaglezr.infinitymirror} usage.
 * <p>
 * The major inclusion is for support of out-putting information to a {@link Label} for display in the GUI.
 *
 * @author Mark Zeagler
 * @version 0.9
 */
public class IMLoggingTool extends LoggingTool {

	private static IMLoggingTool logger;

	public enum Printers {
		LOG_PRINTER( "log" ), CONSOLE_PRINTER( "console" ), LABEL_PRINTER( "label" );

		public final String NAME;

		Printers( String name ) {
			this.NAME = name;
		}
	}

	public enum UserTypes {
		CLIENT( "client" ), SERVER( "server" ), APP( "app" ), TEST( "test" );

		public final String NAME;

		UserTypes( String name ) {
			this.NAME = name;
		}
	}

	private static Label outputLabel;

	/**
	 * For use when there is a label
	 *
	 * @param userType
	 * @param label
	 * @param defaultPrinter
	 */
	public static IMLoggingTool startLogger( UserTypes userType, Label label, Printers defaultPrinter ) {
		logger = new IMLoggingTool( userType, label, defaultPrinter );
		return logger;
	}

	private IMLoggingTool( UserTypes userType, Label label, Printers defaultPrinter ) {
		Consumer<String> logPrinter = generateLogPrinter( "im_" + userType.NAME );
		outputLabel = label;
		Consumer<String> labelPrinter = generateLabelPrinter( label );

		addAllPrinters( logPrinter, labelPrinter );

		if ( defaultPrinter == Printers.CONSOLE_PRINTER ) {
			printer = printers.get( 0 );
		} else if ( defaultPrinter == Printers.LOG_PRINTER ) {
			printer = logPrinter;
		} else if ( defaultPrinter == Printers.LABEL_PRINTER ) {
			printer = labelPrinter;
		} else {
			printer = printers.get( 0 );
			printAll( "Default printer could not be set" );
		}
	}

	public static synchronized IMLoggingTool getLogger() {
		if ( logger == null ) {
			LoggingTool.getLogger();
			logger.printAll( Error.IM_LOG_NULL.getText() );
		}
		return logger;
	}

	public static synchronized void print( Error error ) {
		LoggingTool.print( error.getText() );
	}

	public static synchronized void printAll( Error error ) {
		LoggingTool.printAll( error.getText() );
	}

	/**
	 * Generates a printer printer that prints a String to a {@link javafx.scene.control.Label}
	 *
	 * @param label
	 * @return
	 */
	public static Consumer<String> generateLabelPrinter( Label label ) {
		Consumer<String> printer = new RunnableLabelPrinter( label );
		return printer;
	}

	public static Label getLabel() {
		return outputLabel;
	}

	/**
	 * 	Added to circumvent threading error in printAll()
	 */
	public static class RunnableLabelPrinter implements Runnable, Consumer<String> {

		String s;
		Label label;

		public RunnableLabelPrinter( Label label ) {
			this.label = label;

		}

		public void accept( String s ) {
			this.s = s;
			Platform.runLater( this );
		}

		@Override public void run() {
			label.setText( s );
		}
	}
}
