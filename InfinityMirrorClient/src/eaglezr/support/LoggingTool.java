package eaglezr.support;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Consumer;

import javafx.concurrent.Task;

/**
 * Generates a logging tool that can be set to either print to the Console or to a log file with ease.
 *
 * @author Mark Zeagler
 * @version 0.9
 */
public class LoggingTool implements Closeable {

	// LATER Check if there is a to save/load this from a file
	private static final int NUM_LOGS_TO_KEEP = 5;

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

	protected static LoggingTool logger;

	protected final Printers defaultPrinter;
	protected final Consumer<String> printer;

	protected final Consumer<String> logPrinter;
	protected final Consumer<String> consolePrinter;

	/**
	 * Creates a {@link LoggingTool} with the specified default printer and using the root name to generate the log
	 * file.
	 *
	 * @param defaultPrinter The default printer that will be printed to when the generic print(String) function is
	 *                       called.
	 * @param rootLogName    The root name for the log file name.
	 * @return The constructed {@link LoggingTool}.
	 */
	public static LoggingTool startLogger( Printers defaultPrinter, String rootLogName ) {
		if ( logger == null || !( logger.defaultPrinter == defaultPrinter ) ) {
			logger = new LoggingTool( defaultPrinter, rootLogName );
		}
		return logger;
	}

	private LoggingTool( Printers defaultPrinter, String rootLogName ) {
		new Thread( new ClearLogs() ).start();
		this.logPrinter = generateLogPrinter( rootLogName );
		this.consolePrinter = generateConsolePrinter();

		if ( defaultPrinter == Printers.CONSOLE_PRINTER ) {
			this.defaultPrinter = defaultPrinter;
			this.printer = this.consolePrinter;
		} else if ( defaultPrinter == Printers.LOG_PRINTER ) {
			this.defaultPrinter = defaultPrinter;
			this.printer = this.logPrinter;
		} else {
			System.out.println( "Default printer could not be set" );
		}
	}

	protected LoggingTool( String outputFileRootName ) {
		new Thread( new ClearLogs() ).start();
		this.logPrinter = generateLogPrinter( outputFileRootName );
		this.consolePrinter = generateConsolePrinter();
	}

	/**
	 * @return The current {@link LoggingTool} or {@link null} if there isn't one.
	 */
	public static LoggingTool getLogger() {
		return logger;
	}

	/**
	 * Generates a printer that prints a String to the log file. The file will be located at "logs\rootName_log
	 * YYYY_MM_DD_hh_mm_ss.txt", where the time corresponds to the system time when the log is created.
	 *
	 * @param rootName The root name of the file to be printed to.
	 * @return A {@link Consumer} that prints to a new log file with the given root name.
	 */
	public static Consumer<String> generateLogPrinter( String rootName ) {
		GregorianCalendar calendar = new GregorianCalendar();
		File logFile = new File( "logs\\" + rootName + "_log " + calendar.get( GregorianCalendar.YEAR ) + "_" + (
				calendar.get( GregorianCalendar.MONTH ) + 1 ) + "_" + calendar.get( GregorianCalendar.DAY_OF_MONTH )
				+ "_" + calendar.get( GregorianCalendar.HOUR_OF_DAY ) + "_" + calendar.get( GregorianCalendar.MINUTE )
				+ "_" + calendar.get( GregorianCalendar.SECOND ) + ".txt" );
		Consumer<String> printer;
		try {
			// LATER Check if the file or directory exists first?
			logFile.getParentFile().mkdirs();
			logFile.createNewFile();
			@SuppressWarnings( "resource" ) PrintStream outputStream = new PrintStream( logFile );
			printer = s -> {
				outputStream.println( new Date( System.currentTimeMillis() ).toString() + ": " + s );
			};
		} catch ( FileNotFoundException e ) {
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
	 * Generates a log printer that prints to the give {@link File}.
	 * @param outputFile The file for the log printer to print into.
	 * @return A {@link Consumer} that prints a String into the {@link File}.
	 */
	public static Consumer<String> generateLogPrinter( File outputFile ) {
		Consumer<String> printer;
		try {
			outputFile.getParentFile().mkdirs();
			outputFile.createNewFile();
			@SuppressWarnings( "resource" ) PrintStream outputStream = new PrintStream( outputFile );
			printer = s -> {
				outputStream.println( new Date( System.currentTimeMillis() ).toString() + ": " + s );
			};
		} catch ( FileNotFoundException e ) {
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
		if ( consolePrinter != null ) {
			consolePrinter.accept( s );
		}
		if ( logPrinter != null ) {
			logPrinter.accept( s );
		}
	}

	/**
	 * Manages the build-up of log files
	 */
	public void close() {

		logger = null;
		new Thread( new ClearLogs() ).start();
	}

	@SuppressWarnings( "rawtypes" ) private class ClearLogs extends Task {

		@Override protected Object call() throws Exception {
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
				} else if ( files[i].getName().contains( "LoggingTool_log" ) ) {
					myFiles.add( files[i] );
				}
			}

			// Deletes empty files
			for ( int i = 0; i < filesToDelete.size(); i++ ) {
				filesToDelete.get( i ).delete();
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
		 * @param left
		 * @param right
		 * @return true if {@linkleft} is older, false if {@linkright} is older
		 */
		private boolean getOlder( String left, String right ) {
			return left.compareTo( right ) < 0;
		}

		public void succeeded() {
			super.succeeded();
		}

		public void failed() {
			super.failed();
			print( "Logs could not be cleared" );
		}
	}
}
