package eaglezr.infinitymirror.support;

import java.io.Serializable;
import java.util.Comparator;

import eaglezr.support.LoggingTool;

import javafx.scene.paint.Color;

/**
 * The data representation of the Infinity Mirror's status that will be passed between the clients and server.
 *
 * @author Mark Zeagler
 */
public final class InfinityMirror implements Serializable, Comparable<InfinityMirror> {

	/**
	 * Auto-generated Serial ID
	 */
	private static final long serialVersionUID = 827621314763779941L;

	public enum Mode {
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

		Mode( int command ) {
			this.COMMAND = command;
		}
	}

	// TODO InfinityMirror accessors
	private final boolean lightsOn;
	private final boolean whiteLightModeOn;

	private final Mode currMode;
	private final Color[] lights;

	private final Color primaryColor;
	private final Color secondaryColor;

	private InfinityMirror( Mode mode ) {
		this( mode, null, Color.BLACK );
		// LATER Throw EMS error if mode isn't 1, 2, 5, or 6?
	}

	public InfinityMirror( Mode mode, Color primaryColor ) {
		this( mode, primaryColor, Color.BLACK );
		// LATER Throw EMS error if Mode is not 3?
	}

	public InfinityMirror( Mode mode, Color primaryColor, Color secondaryColor ) {
		this.currMode = mode;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.lightsOn = true;
		this.whiteLightModeOn = false;
		this.lights = setLights( new Color[180], getMode() );
	}

	private InfinityMirror( InfinityMirror mirror, boolean lightsOn, boolean whiteLightModeOn ) {
		this.lightsOn = lightsOn;
		this.whiteLightModeOn = whiteLightModeOn;
		this.currMode = mirror.currMode;
		this.lights = setLights( new Color[180], getMode() );
		this.primaryColor = mirror.primaryColor;
		this.secondaryColor = mirror.secondaryColor;
	}

	InfinityMirror( InfinityMirrorPreview preview ) {
		this.lightsOn = preview.lightsOn;
		this.whiteLightModeOn = preview.whiteLightModeOn;
		this.currMode = preview.currMode;
		this.lights = setLights( new Color[180], getMode() );
		this.primaryColor = preview.primaryColor;
		this.secondaryColor = preview.secondaryColor;
	}

	/**
	 * Determines the current {@link Mode} that this object is in.
	 *
	 * @return The current {@link Mode} in use.
	 */
	public Mode getMode() {
		return ( this.lightsOn ? ( this.whiteLightModeOn ? Mode.WHITE_MODE : this.currMode ) : Mode.LIGHTS_OFF );
	}

	public Color[] getLights() {
		if ( !this.lightsOn ) {
			return setAllLights( new Color[180], Color.BLACK );
		} else if ( this.whiteLightModeOn ) {
			return setAllLights( new Color[180], Color.WHITE );
		} else {
			return this.lights;
		}
	}

	public Color getPrimaryColor() {
		if ( !this.lightsOn ) {
			return Color.BLACK;
		} else if ( this.whiteLightModeOn ) {
			return Color.WHITE;
		} else {
			return this.primaryColor;
		}
	}

	public Color getSecondaryColor() {
		if ( !this.lightsOn ) {
			return Color.BLACK;
		} else if ( this.whiteLightModeOn ) {
			return Color.WHITE;
		} else {
			return this.secondaryColor;
		}
	}

	/**
	 * Creates a new {@link InfinityMirror} instance with the lightsOn value reversed. <p>The new object must be created
	 * because this class is immutable.</p>
	 *
	 * @return A new instance of the class with everything copied except the lightsOn variable, which is reversed.
	 */
	public InfinityMirror getToggleLights() {
		LoggingTool.getLogger()
				.print( "Making a new InfinityMirror. Make sure you're saving it and not just throwing it away..." );
		return new InfinityMirror( this, !this.lightsOn, this.whiteLightModeOn );
	}

	public InfinityMirror getToggleWhiteLightMode() {
		LoggingTool.getLogger()
				.print( "Making a new InfinityMirror. Make sure you're saving it and not just throwing it away..." );
		return new InfinityMirror( this, this.lightsOn, !this.whiteLightModeOn );
	}

	public int compareTo( InfinityMirror m2 ) {
		int total = 0;
		total += compare( this.lightsOn, m2.lightsOn );
		total += compare( this.whiteLightModeOn, m2.whiteLightModeOn );
		total += this.currMode.compareTo( m2.currMode );
		// Ignore comparing lights. It's a derived value, anyways.
		total += compare( this.primaryColor, m2.primaryColor );
		total += compare( this.secondaryColor, m2.secondaryColor );
		return total;
	}

	public boolean equals( InfinityMirror m2 ) {
		return compareTo( m2 ) == 0;
	}

	// LATER Fix setLights()
	private static Color[] setLights( Color[] lights, Mode mode, Color primaryColor ) {
		return setLights( lights, mode, primaryColor, null );
	}

	private static Color[] setLights( Color[] lights, Mode mode ) {
		return setLights( lights, mode, null, null );
	}

	// LATER Figure out how best to set the lights
	private static Color[] setLights( Color[] lights, Mode mode, Color primaryColor, Color secondaryColor ) {
		if ( mode == Mode.LIGHTS_OFF ) {
			return setAllLights( lights, Color.BLACK );
		} else if ( mode == Mode.WHITE_MODE ) {
			return setAllLights( lights, Color.WHITE );
		} else if ( mode == Mode.SOLID_COLOR_MODE ) {
			return setAllLights( lights, primaryColor );
		} else if ( mode == Mode.ALTERNATING_COLOR_MODE ) {
			return setAlternateLights( lights, primaryColor, secondaryColor );
		} else if ( mode == Mode.RAINBOW_MODE ) {
			// TODO Rainbow Mode Lights
		} else if ( mode == Mode.RAINBOW_PULSE_MODE ) {
			// TODO Rainbow Pulse Mode Lights
			// Set Lights --> Set new Thread that "Pulses" lights?
		} else if ( mode == Mode.PULSE_MODE ) {
			// TODO Pulse Mode Lights
		} else {
			// LATER Print "Invalid Mode" error to EMS
		}
		return lights; // TODO Fix above return statements and remove this
	}

	private static Color[] setAllLights( Color[] lights, Color color ) {
		if ( color == null ) {
			// LATER Print "Null Color" error to EMS
		} else {
			for ( int i = 0; i < lights.length; i++ ) {
				lights[i] = color;
			}
		}
		return lights; // TODO Fix above return statements and remove this
	}

	private static Color[] setAlternateLights( Color[] lights, Color color1, Color color2 ) {
		if ( color1 == null ) {
			// LATER Print "Null Primary Color" error to EMS
		} else if ( color2 == null ) {
			// LATER Print "Null Secondary Color" error to EMS
		} else {
			for ( int i = 0; i < lights.length; i++ ) {
				if ( i % 2 == 0 ) {
					lights[i] = color1;
				} else {
					lights[i] = color2;
				}
			}
		}
		return lights; // TODO Fix above return statements and remove this
	}

	// https://stackoverflow.com/questions/13229592/implement-comparator-for-primitive-boolean-type
	private static int compare( boolean b1, boolean b2 ) {
		return ( b1 == b2 ? 0 : ( b1 ? 1 : -1 ) );
	}

	private static int compare( Color c1, Color c2 ) {
		return c1.toString().compareTo( c2.toString() );
	}
}
