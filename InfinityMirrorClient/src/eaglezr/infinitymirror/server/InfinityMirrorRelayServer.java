package eaglezr.infinitymirror.server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import eaglezr.infinitymirror.support.ClientCommands;
import eaglezr.infinitymirror.support.ErrorManagementSystem;

public class InfinityMirrorRelayServer {

	// Server variables
	private static int port = 11896;
	private static DataInputStream in;
	private static DataOutputStream out;
	private static ServerSocket serverSocket;

	// Lamp Status Variables
	private static boolean lightsOn = true;
	private static boolean whiteLightMode = false;

	private static Consumer<String> printer;

	public static void main(String[] args) {
		printer = ErrorManagementSystem.generateConsolePrinter();

		try { // If this breaks, we're SOL, and the program will need to restart
			serverSocket = new ServerSocket(port);
			// File log = new File("log" + )
			while (true) {
				try {
					Socket connection = serverSocket.accept();
					in = new DataInputStream(connection.getInputStream());
					out = new DataOutputStream(connection.getOutputStream());
					try { // Assuming misread input
						int input = in.read();
						printer.accept("Read: " + input);
						int output = -1;
						if (input == ClientCommands.LIGHTS.COMMAND) { // Toggle
																		// on/off
							lightsOn = !lightsOn;
							// TODO Communicate with controller?
							output = (lightsOn ? 11 : 10);
						} else if (input == 2) { // Toggle white light
							whiteLightMode = !whiteLightMode;
							// TODO Communicate with controller?
							output = (whiteLightMode ? 21 : 20);
						} else if (input == 3) { // Solid Color Mode
							output = 3; // echo command
							// TODO Figure out how to read a color
							// TODO Set color to primary color, and change mode
						} else if (input == 4) { // Alternating Color Mode
							// TODO Handle command
						} else if (input == 5) { // Rainbow Mode
							// TODO Handle Rainbow Mode command
						} else if (input == 6) { // Rainbow Pulse Mode
							// TODO Handle Rainbow Pulse Mode command
						} else if (input == 7) {
							// TODO Handle Pulse Mode command
						} else { // Input not recognized
							out.writeInt(-1); // error response
						}
						printer.accept("Returned: " + output);
						out.write(output);
					} catch (IOException e) {
						out.writeInt(-1); // error response
						printer.accept("There was an error sending the message");
					}
					connection.close();
				} catch (IOException e) { // Trouble opening the connection
					printer.accept("Connection cannot be made.");
				}
			}
		} catch (IOException e) {
			printer.accept("The socket could not be opened.");
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				printer.accept("The socket could not be closed.");
			}
		}
	}
}
