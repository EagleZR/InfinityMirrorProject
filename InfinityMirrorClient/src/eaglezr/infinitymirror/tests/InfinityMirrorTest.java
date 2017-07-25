package eaglezr.infinitymirror.tests;

import eaglezr.infinitymirror.support.InfinityMirror;
import eaglezr.infinitymirror.support.InfinityMirrorPreview;
import eaglezr.infinitymirror.support.IMLoggingTool;
import eaglezr.support.LoggingTool;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InfinityMirrorTest {

	@BeforeAll static void setup(){
		// Ugh, so uncivilized
		IMLoggingTool.startLogger( LoggingTool.Printers.CONSOLE_PRINTER, InfinityMirrorTest.class.getSimpleName() );
	}

	@Test void testConstructors() {
		// Test with 2 colors
		InfinityMirror mirror = new InfinityMirror( InfinityMirror.Mode.PULSE_MODE, Color.BLACK, Color.ANTIQUEWHITE );
		assertTrue( mirror.getMode().equals( InfinityMirror.Mode.PULSE_MODE ) );
		assertTrue( mirror.getPrimaryColor().equals( Color.BLACK ) );
		assertTrue( mirror.getSecondaryColor().equals( Color.ANTIQUEWHITE ) );

		// Test with 1 color
		mirror = new InfinityMirror( InfinityMirror.Mode.SOLID_COLOR_MODE, Color.BLUE );
		assertTrue( mirror.getMode().equals( InfinityMirror.Mode.SOLID_COLOR_MODE ) );
		assertTrue( mirror.getPrimaryColor().equals( Color.BLUE ) );
		assertTrue( mirror.getSecondaryColor().equals( Color.BLACK ) );

		// Test with "lights off"
		mirror = mirror.toggleLights();
		assertTrue( mirror.getMode().equals( InfinityMirror.Mode.LIGHTS_OFF ) );
		assertTrue( mirror.getPrimaryColor().equals( Color.BLACK ) );
		assertTrue( mirror.getSecondaryColor().equals( Color.BLACK ) );
		Color[] lights = mirror.getLights();
		for ( Color light : lights ) {
			assertTrue( light.equals( Color.BLACK ) );
		}

		// Test with white light mode on
		mirror = mirror.toggleWhiteLightMode();
		mirror = mirror.toggleLights();
		assertTrue( mirror.getMode().equals( InfinityMirror.Mode.WHITE_MODE ) );
		assertTrue( mirror.getPrimaryColor().equals( Color.WHITE ) );
		assertTrue( mirror.getSecondaryColor().equals( Color.WHITE ) );
		lights = mirror.getLights();
		for ( Color light : lights ) {
			assertTrue( light.equals( Color.WHITE ) );
		}
	}

	@Test void testPreview() {
		InfinityMirrorPreview preview = new InfinityMirrorPreview();
		preview.currMode = InfinityMirror.Mode.SOLID_COLOR_MODE;
		preview.primaryColor = Color.BLUE;
		InfinityMirror m1 = preview.generateInfinityMirror();
		InfinityMirror m2 = new InfinityMirror( InfinityMirror.Mode.SOLID_COLOR_MODE, Color.BLUE );

		System.out.println( m1.compare( m1, m2 ) );
		assertTrue( m1.getMode().equals( m2.getMode() ) );
		assertTrue( m1.getPrimaryColor().equals( m2.getPrimaryColor() ) );
		assertTrue( m1.getSecondaryColor().equals( m2.getSecondaryColor() ) );
		// FIXME InfinityMirror.equals is broken
		assertTrue( m1.equals( m2 ) );
	}

}
