package eaglezr.infinitymirror.support;

import java.io.Serializable;

import eaglezr.support.logs.LoggingTool;

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

	final boolean lightsOn;
	final boolean whiteLightModeOn;

	final Mode currMode;
	// final Color[] lights;

	final MyColor primaryColor;
	final MyColor secondaryColor;

	private InfinityMirror( Mode mode ) {
		this( mode, Color.BLACK, Color.BLACK );
		// LATER Throw log error if mode isn't 1, 2, 5, or 6?
	}

	public InfinityMirror( Mode mode, Color primaryColor ) {
		this( mode, primaryColor, Color.BLACK );
		// LATER Throw log error if Mode is not 3?
	}

	public InfinityMirror( Mode mode, Color primaryColor, Color secondaryColor ) {
		this.currMode = mode;
		this.primaryColor = new MyColor( primaryColor );
		this.secondaryColor = new MyColor( secondaryColor );
		this.lightsOn = true;
		this.whiteLightModeOn = false;
		//this.lights = setLights( new Color[180], getMode(), getPrimaryColor(), getSecondaryColor() );
	}

	private InfinityMirror( InfinityMirror mirror, boolean lightsOn, boolean whiteLightModeOn ) {
		this.lightsOn = lightsOn;
		this.whiteLightModeOn = whiteLightModeOn;
		this.currMode = mirror.currMode;
		//this.lights = setLights( new Color[180], getMode(), getPrimaryColor(), getSecondaryColor() );
		this.primaryColor = mirror.primaryColor;
		this.secondaryColor = mirror.secondaryColor;
	}

	InfinityMirror( InfinityMirrorPreview preview ) {
		this.lightsOn = preview.lightsOn;
		this.whiteLightModeOn = preview.whiteLightModeOn;
		this.currMode = preview.currMode;
		//this.lights = setLights( new Color[180], getMode(), getPrimaryColor(), getSecondaryColor() );
		this.primaryColor = new MyColor( preview.primaryColor );
		this.secondaryColor = new MyColor( preview.secondaryColor );
	}

	/**
	 * Determines the current {@link Mode} that this object is in.
	 *
	 * @return The current {@link Mode} in use.
	 */
	public Mode getMode() {
		return ( this.lightsOn ? ( this.whiteLightModeOn ? Mode.WHITE_MODE : this.currMode ) : Mode.LIGHTS_OFF );
	}

//	public Color[] getLights() {
//		if ( !this.lightsOn ) {
//			return setAllLights( new Color[180], Color.BLACK );
//		} else if ( this.whiteLightModeOn ) {
//			return setAllLights( new Color[180], Color.WHITE );
//		} else {
//			//return this.lights;
//		}
//	}

	public Color getPrimaryColor() {
		if ( !this.lightsOn ) {
			return Color.BLACK;
		} else if ( this.whiteLightModeOn ) {
			return Color.WHITE;
		} else {
			return this.primaryColor.getColor();
		}
	}

	public Color getSecondaryColor() {
		if ( !this.lightsOn ) {
			return Color.BLACK;
		} else if ( this.whiteLightModeOn ) {
			return Color.WHITE;
		} else {
			return this.secondaryColor.getColor();
		}
	}

	/**
	 * Creates a new {@link InfinityMirror} instance with the lightsOn value reversed. <p>The new object must be created
	 * because this class is immutable.</p>
	 *
	 * @return A new instance of the class with everything copied except the lightsOn variable, which is reversed.
	 */
	public InfinityMirror getToggleLights() {
		LoggingTool.print( "Making a new InfinityMirror. Make sure you're saving it and not just throwing it away..." );
		return new InfinityMirror( this, !this.lightsOn, this.whiteLightModeOn );
	}

	public InfinityMirror getToggleWhiteLightMode() {
		LoggingTool.print( "Making a new InfinityMirror. Make sure you're saving it and not just throwing it away..." );
		return new InfinityMirror( this, this.lightsOn, !this.whiteLightModeOn );
	}

	public int compareTo( InfinityMirror m2 ) {
		int total = 0;
		total += compare( this.lightsOn, m2.lightsOn );
		total += compare( this.whiteLightModeOn, m2.whiteLightModeOn );
		total += this.currMode.compareTo( m2.currMode );
		// Ignore comparing lights. It's a derived value, anyways.
		total += compare( this.primaryColor.getColor(), m2.primaryColor.getColor() );
		total += compare( this.secondaryColor.getColor(), m2.secondaryColor.getColor() );
		return total;
	}

	public boolean equals( InfinityMirror m2 ) {
		return compareTo( m2 ) == 0;
	}

	public String toString() {
		return getMode().name() + ": " + getPrimaryColor() + ", " + getSecondaryColor();
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
			// LATER Print "Invalid Mode" error to log
		}
		return lights; // TODO Fix above return statements and remove this
	}

	private static Color[] setAllLights( Color[] lights, Color color ) {
		if ( color == null ) {
			// LATER Print "Null MyColor" error to log
		} else {
			for ( int i = 0; i < lights.length; i++ ) {
				lights[i] = color;
			}
		}
		return lights; // TODO Fix above return statements and remove this
	}

	private static Color[] setAlternateLights( Color[] lights, Color color1, Color color2 ) {
		if ( color1 == null ) {
			// LATER Print "Null Primary MyColor" error to log
		} else if ( color2 == null ) {
			// LATER Print "Null Secondary MyColor" error to log
		} else {
			for ( int i = 0; i < lights.length; i++ ) {
				if ( i % 2 == 0 ) {
					lights[i] = color1;
				} else {
					lights[i] = color2;
				}
			}
		}
		return lights;
	}

	// https://stackoverflow.com/questions/13229592/implement-comparator-for-primitive-boolean-type
	private static int compare( boolean b1, boolean b2 ) {
		return ( b1 == b2 ? 0 : ( b1 ? 1 : -1 ) );
	}

	private static int compare( Color c1, Color c2 ) {
		return c1.toString().compareTo( c2.toString() );
	}

	public static class MyColor implements Serializable {

		private static final long serialVersionUID = -6377660736345177025L;
		private double red;
		private double green;
		private double blue;
		private double opacity;

		public MyColor( Color color ) {
			this.red = color.getRed();
			this.blue = color.getBlue();
			this.green = color.getGreen();
			this.opacity = color.getOpacity();
		}

		public MyColor(double red, double green, double blue, double opacity) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.opacity = opacity;
		}

		public Color getColor () {
			return new Color( red, green, blue, opacity );
		}

		public double getRed() {
			return this.red;
		}

		public double getBlue() {
			return this.blue;
		}

		public double getGreen() {
			return this.green;
		}

	}
}
