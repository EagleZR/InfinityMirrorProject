
package eaglezr.infinitymirror.support;

import javafx.scene.paint.Color;

public final class InfinityMirror {
	
	public static enum Modes {
		LIGHTS( 1 ), WHITE_MODE( 2 ), SOLID_COLOR_MODE( 3 ), ALTERNATING_COLOR_MODE( 4 ), RAINBOW_MODE( 5 ), // Un-moving,
																												// multi-colored
		RAINBOW_PULSE_MODE( 6 ), // Multi-colored pulse mode
		PULSE_MODE( 7 ); // 2-tone pulse mode, moving
		
		public final int COMMAND;
		
		Modes( int command ) {
			this.COMMAND = command;
		}
	}
	
	private final Modes currMode;
	private final Color primaryColor;
	private final Color secondaryColor;
	private final Color[] lights = new Color[180];
	
	public InfinityMirror( Modes mode ) {
		this.currMode = mode;
		this.primaryColor = null;
		this.secondaryColor = null;
		setLights( this.lights, this.currMode );
	}
	
	public InfinityMirror( Modes mode, Color primaryColor ) {
		this.currMode = mode;
		this.primaryColor = primaryColor;
		this.secondaryColor = null;
		setLights( this.lights, this.currMode );
	}
	
	public InfinityMirror( Modes mode, Color primaryColor, Color secondaryColor ) {
		this.currMode = mode;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		setLights( this.lights, this.currMode );
	}
	
	// TODO Figure out how best to set the lights
	public static void setLights( Color[] lights, Modes mode ) {
		
	}
}
