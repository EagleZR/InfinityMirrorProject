package eaglezr.support.logs;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Consumer;

public abstract class LoggingTool implements Closeable {

	// LATER Check if there is a to save/load this from a file
	private static final int NUM_LOGS_TO_KEEP = 5;

	/**
	 * Prints a String through the default printer.
	 *
	 * @param s The String to be printed.
	 */
	public abstract void print(String s);

	/**
	 * Prints a String through all of the printers.
	 *
	 * @param s The String to be printed.
	 */
	public abstract void printAll(String s);

	/**
	 * Generates a printer that prints a String to the console
	 *
	 * @return
	 */
	public static Consumer<String> generateConsolePrinter() {
		return s -> System.out.println( new Date( System.currentTimeMillis() ).toString() + ": " + s );
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
	 * Manages the build-up of log files
	 */
	public void close() {
		new Thread( new ClearLogs() ).start();
	}

	protected class ClearLogs implements Runnable {

		public void run() {
			try {
				purgeLogs();
			} catch ( FileNotFoundException e ) {
				printAll("Logs could not be cleared");
			}
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
			for ( File file : filesToDelete ) {
				file.delete();
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
	}
}
