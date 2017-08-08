package eaglezr.support.logs;

import java.util.function.Consumer;

/**
 * Generates a logging tool that can be set to either print to the Console or to a log file with ease.
 *
 * @author Mark Zeagler
 * @version 0.9
 */
public class GenericLoggingTool extends LoggingTool {

	/**
	 * These labels enable the specification of which printer is being referenced.
	 */
	public enum Printers {
		LOG_PRINTER( "log" ), CONSOLE_PRINTER( "console" );

		public final String NAME;

		Printers( String name ) {
			this.NAME = name;
		}
	}

	private static GenericLoggingTool logger;

	private final Printers defaultPrinter;
	private final Consumer<String> printer;

	private final Consumer<String> logPrinter;
	private final Consumer<String> consolePrinter;

	/**
	 * Creates a {@link GenericLoggingTool} with the specified default printer and using the root name to generate the log
	 * file.
	 *
	 * @param defaultPrinter The default printer that will be printed to when the generic print(String) function is
	 *                       called.
	 * @param rootLogName    The root name for the log file name.
	 * @return The constructed {@link GenericLoggingTool}.
	 */
	public static GenericLoggingTool startLogger( Printers defaultPrinter, String rootLogName ) {
		if ( logger == null || !( logger.defaultPrinter == defaultPrinter ) ) {
			logger = new GenericLoggingTool( defaultPrinter, rootLogName );
		}
		return logger;
	}

	private GenericLoggingTool( Printers defaultPrinter, String rootLogName ) {
		this.logPrinter = generateLogPrinter( rootLogName );
		this.consolePrinter = generateConsolePrinter();

		if ( defaultPrinter == Printers.CONSOLE_PRINTER ) {
			this.defaultPrinter = defaultPrinter;
			this.printer = this.consolePrinter;
		} else if ( defaultPrinter == Printers.LOG_PRINTER ) {
			this.defaultPrinter = defaultPrinter;
			this.printer = this.logPrinter;
		} else {
			this.defaultPrinter = Printers.CONSOLE_PRINTER;
			this.printer = this.consolePrinter;
			System.out.println( "Default printer could not be set" );
		}
	}

	/**
	 * @return The current {@link GenericLoggingTool} or {@link null} if there isn't one.
	 */
	public static GenericLoggingTool getLogger() {
		return logger;
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
		if ( consolePrinter != null ) {
			consolePrinter.accept( s );
		}
		if ( logPrinter != null ) {
			logPrinter.accept( s );
		}
	}
}
