package eaglezr.infinitymirror.support;

import javafx.scene.paint.Color;

/**
 * Constructs a changeable form of the {@link InfinityMirror} that can be finalized to produce the immutable {@link InfinityMirror}.
 */
public class InfinityMirrorPreview {

	public boolean lightsOn = true;
	public boolean whiteLightModeOn = false;

	public InfinityMirror.Mode currMode;

	public Color primaryColor = Color.BLACK;
	public Color secondaryColor = Color.BLACK;

	public InfinityMirrorPreview() {

	}

	public InfinityMirrorPreview(InfinityMirror currMirror) {
		this.lightsOn = currMirror.lightsOn;
		this.whiteLightModeOn = currMirror.whiteLightModeOn;

		this.currMode = currMirror.currMode;

		this.primaryColor = currMirror.primaryColor.getColor();
		this.secondaryColor = currMirror.secondaryColor.getColor();

	}

	public InfinityMirror generateInfinityMirror() {
		return new InfinityMirror( this );
	}
}
