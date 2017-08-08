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
 */
public class IMLoggingTool extends LoggingTool {

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

	private Label outputLabel;

	private static IMLoggingTool logger;
	private Consumer<String> printer;

	private final Printers defaultPrinter;
	private final Consumer<String> logPrinter;
	private final Consumer<String> consolePrinter;
	private Consumer<String> labelPrinter;

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
		this.logPrinter = generateLogPrinter( "im_" + userType.NAME );
		this.outputLabel = label;
		this.labelPrinter = generateLabelPrinter( label );
		this.consolePrinter = generateConsolePrinter();

		if ( defaultPrinter == Printers.CONSOLE_PRINTER ) {
			this.defaultPrinter = defaultPrinter;
			this.printer = this.consolePrinter;
		} else if ( defaultPrinter == Printers.LOG_PRINTER ) {
			this.defaultPrinter = defaultPrinter;
			this.printer = this.logPrinter;
		} else if ( defaultPrinter == Printers.LABEL_PRINTER ) {
			this.defaultPrinter = defaultPrinter;
			this.printer = this.labelPrinter;
		} else {
			this.defaultPrinter = Printers.CONSOLE_PRINTER;
			this.printer = this.consolePrinter;
			System.out.println( "Default printer could not be set" );
		}
	}

	public static IMLoggingTool getLogger() {
		if ( logger != null && logger.getClass() == IMLoggingTool.class ) {
			return logger;
		} else {
			logger.printAll( Error.IM_LOG_NULL.getText() );
			return null;
		}
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

	/**
	 * Prints a String through the default printer.
	 *
	 * @param s The String to be printed.
	 */
	public synchronized void print( String s ) {
		if ( this.printer != null ) {
			this.printer.accept( s );
		} else {
			printAll( "There is no set printer" );
		}
	}

	/**
	 * Prints a String through all of the printers.
	 *
	 * @param s The String to be printed.
	 */
	public synchronized void printAll( String s ) {
		if ( labelPrinter != null ) {
			labelPrinter.accept( s );
		}
		if ( consolePrinter != null ) {
			consolePrinter.accept( s );
		}
		if ( logPrinter != null ) {
			logPrinter.accept( s );
		}
	}

	public Label getLabel() {
		return this.outputLabel;
	}

	// Added to circumvent threading error in printAll()
	private static class RunnableLabelPrinter implements Runnable, Consumer<String> {

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
