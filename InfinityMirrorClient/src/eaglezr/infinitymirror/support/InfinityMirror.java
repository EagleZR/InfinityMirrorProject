
package eaglezr.infinitymirror.support;

import java.io.Serializable;
import javafx.scene.paint.Color;

public final class InfinityMirror implements Serializable {
	
	/**
	 * Auto-generated Serial ID
	 */
	private static final long serialVersionUID = 827621314763779941L;
	
	public static enum Modes {
		// @formatter:off
		LIGHTS_OFF( 1 ), 
		WHITE_MODE( 2 ), 
		SOLID_COLOR_MODE( 3 ), 
		ALTERNATING_COLOR_MODE( 4 ), 
		RAINBOW_MODE( 5 ), // Un-moving, multi-colored
		RAINBOW_PULSE_MODE( 6 ), // Multi-colored pulse mode
		PULSE_MODE( 7 ); // 2-tone pulse mode, moving
		// @formatter:on
		
		public final int COMMAND;
		
		private int offset = 0;
		private int offsetMax = 180;
		
		Modes( int command ) {
			this.COMMAND = command;
		}
		
		private Color primaryColor;
		private Color secondaryColor;
		
		public void setColors( Color setPrimary, Color setSecondary ) {
			setPrimaryColor( setPrimary );
			setSecondaryColor( setSecondary );
		}
		
		public void setPrimaryColor( Color setPrimary ) {
			if ( COMMAND == 3 || COMMAND == 4 || COMMAND == 7 ) {
				this.primaryColor = setPrimary;
			}
		}
		
		public void setSecondaryColor( Color setSecondary ) {
			if ( COMMAND == 4 || COMMAND == 7 ) {
				this.secondaryColor = setSecondary;
			}
		}
		
		public Color getPrimary() {
			return this.primaryColor;
		}
		
		public Color getSecondary() {
			return this.secondaryColor;
		}
		
		public int getOffset() {
			return this.offset;
		}
		
		public void incrementOffset() {
			offset = ++offset % ( offsetMax - 1 );
		}
	}
	
	private final Modes currMode;
	private final Color[] lights = new Color[180];
	private final InfinityMirror previous;
	
	public InfinityMirror( Modes mode ) {
		this.currMode = mode;
		setLights( this.lights, this.currMode );
		previous = null;
	}
	
	public InfinityMirror( Modes mode, Color primaryColor ) {
		this.currMode = mode;
		this.currMode.setPrimaryColor( primaryColor );
		setLights( this.lights, this.currMode );
		previous = null;
	}
	
	public InfinityMirror( Modes mode, Color primaryColor, Color secondaryColor ) {
		this.currMode = mode;
		this.currMode.setPrimaryColor( primaryColor );
		this.currMode.setSecondaryColor( secondaryColor );
		setLights( this.lights, this.currMode );
		previous = null;
	}
	
	public InfinityMirror( Modes mode, InfinityMirror previous ) {
		this.currMode = mode;
		setLights( this.lights, this.currMode );
		this.previous = previous;
	}
	
	public InfinityMirror( Modes mode, Color primaryColor, InfinityMirror previous ) {
		this.currMode = mode;
		this.currMode.setPrimaryColor( primaryColor );
		setLights( this.lights, this.currMode );
		this.previous = previous;
	}
	
	public InfinityMirror( Modes mode, Color primaryColor, Color secondaryColor, InfinityMirror previous ) {
		this.currMode = mode;
		this.currMode.setPrimaryColor( primaryColor );
		this.currMode.setSecondaryColor( secondaryColor );
		setLights( this.lights, this.currMode );
		this.previous = previous;
	}
	
	// TODO Figure out how best to set the lights
	private static void setLights( Color[] lights, Modes mode ) {
		if ( mode == Modes.LIGHTS_OFF ) {
			setLights( lights, Color.BLACK );
		} else if ( mode == Modes.WHITE_MODE ) {
			setLights( lights, Color.WHITE );
		} else if ( mode == Modes.SOLID_COLOR_MODE ) {
			setLights( lights, mode.getPrimary() );
		} else if ( mode == Modes.ALTERNATING_COLOR_MODE ) {
			setLights( lights, mode.getPrimary(), mode.getSecondary() );
		} else if (mode == Modes.RAINBOW_MODE) {
			// TODO Rainbow Mode Lights
		} else if (mode == Modes.RAINBOW_PULSE_MODE) {
			// TODO Rainbow Pulse Mode Lights
			// Set Lights --> Set new Thread that "Pulses" lights? 
		} else if (mode == Modes.PULSE_MODE) {
			// TODO Pulse Mode Lights
		} else {
			// TODO Print "Invalid Mode" error to EMS
		}
	}
	
	private static void setLights( Color[] lights, Color color ) {
		if ( color == null ) {
			// TODO Print "Null Color" error to EMS
		} else {
			for ( int i = 0; i < lights.length; i++ ) {
				lights[i] = color;
			}
		}
	}
	
	private static void setLights( Color[] lights, Color color1, Color color2 ) {
		if ( color1 == null ) {
			// TODO Print "Null Primary Color" error to EMS
		} else if ( color2 == null ) {
			// TODO Print "Null Secondary Color" error to EMS
		} else {
			for ( int i = 0; i < lights.length; i++ ) {
				if ( i % 2 == 0 ) {
					lights[i] = color1;
				} else {
					lights[i] = color2;
				}
			}
		}
	}
}
