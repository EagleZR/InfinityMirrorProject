
public enum ClientCommands {
	
	LIGHTS(1), 
	WHITE_MODE(2),
	SOLID_COLOR_MODE(3),
	DESKTOP_HARMONY_MODE(4),
	SOUND_RESPONSIVE_MODE(5),
	MUSIC_RESPONSIVE_MODE(6);

	public final int COMMAND;

	ClientCommands(int command) {
		this.COMMAND = command;
	}
}
