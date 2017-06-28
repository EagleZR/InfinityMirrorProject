
public enum ClientCommands {
	
	LIGHTS(1), 
	WHITE_MODE(2),
	SOLID_COLOR_MODE(3),
	ALTERNATING_COLOR_MODE(4),
	RAINBOW_MODE(5), // Un-moving, multi-colored
	RAINBOW_PULSE_MODE(6), // Multi-colored pulse mode
	PULSE_MODE(7); // 2-tone pulse mode, moving

	public final int COMMAND;

	ClientCommands(int command) {
		this.COMMAND = command;
	}
}
