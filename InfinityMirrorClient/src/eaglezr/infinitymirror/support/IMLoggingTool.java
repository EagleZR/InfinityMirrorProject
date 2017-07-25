
package eaglezr.infinitymirror.support;

import java.util.function.Consumer;
import eaglezr.support.LoggingTool;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * An extension of {@link eaglezr.support.LoggingTool} specifically made for
 * {@link eaglezr.infinitymirror} usage.
 * <p>
 * The major inclusion is for support of out-putting information to a
 * {@link Label} for display in the GUI.
 * 
 * @author Mark Zeagler
 *
 */
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
	 * @param defaultPrinter
	 */
	public static LoggingTool startLogger( UserTypes userType, Label label, Printers defaultPrinter ) {
		logger = new IMLoggingTool( userType, label, defaultPrinter );
		return logger;
	}
	
	private IMLoggingTool( UserTypes userType, Label label, Printers defaultPrinter ) {
		super( "im_" + userType.NAME );
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
		Consumer<String> printer = new RunnableLabelPrinter( label );
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
		if ( labelPrinter != null ) {
			labelPrinter.accept( s );
		}
		super.printAll( s );
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
		
		@Override
		public void run() {
			label.setText( s );
		}
	}
}
