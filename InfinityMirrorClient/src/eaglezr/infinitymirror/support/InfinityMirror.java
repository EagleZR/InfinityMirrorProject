
package eaglezr.infinitymirror.support;

import javafx.scene.paint.Color;

public  class InfinityMirror {
	
	public enum Modes {
		LIGHTS( 1 ), WHITE_MODE( 2 ), SOLID_COLOR_MODE( 3 ), ALTERNATING_COLOR_MODE( 4 ), RAINBOW_MODE( 5 ), // Un-moving,
																												// multi-colored
		RAINBOW_PULSE_MODE( 6 ), // Multi-colored pulse mode
		PULSE_MODE( 7 ); // 2-tone pulse mode, moving
		
		public final int COMMAND;
		
		Modes( int command ) {
			this.COMMAND = command;
		}
	}
	
	private Modes currMode;
	private Color primaryColor;
	private Color secondaryColor;
	private Color[] lights = new Color[180];
	
	public InfinityMirror( Modes mode ) {
		this.currMode = mode;
	}
	
	public InfinityMirror( Modes mode, Color primaryColor ) {
		this( mode );
		this.primaryColor = primaryColor;
	}
	
	public InfinityMirror( Modes mode, Color primaryColor, Color secondaryColor ) {
		this( mode, primaryColor );
		this.secondaryColor = secondaryColor;
	}
	
	public synchronized void setWhiteLightMode() {
		
	}
	
	public synchronized void setSolidColorMode( Color color ) {
		
	}
	
	public synchronized void setAlternatingColorMode( Color primaryColor, Color secondaryColor ) {
		
	}
	
}
