package eaglezr.infinitymirror.support;

public enum Error {
	
	CONNECTION_NOT_MADE(1, "Connection not made."),
	COMMAND_NOT_SENT(2, "Command not sent."),
	INCORRECT_RESPONSE(3, "Received Incorrect Response: "),
	COMMS_THREAD_FAILED(4, "Communications Thread Failed."),
	CONNECTION_REJECTED(5, "The Connection was Rejected."),
	COMMAND_PARSING_ERROR(6, "Command Parsing Error.");
	
	private final String errorText;
	private final int errorCode;
	
	Error (int setCode, String setText) {
		this.errorCode = setCode;
		this.errorText = setText;
	}
	
	public String getText() {
		return this.errorText;
	}
	
	public int getCode() {
		return this.errorCode;
	}
	
	public String toString() {
		return "E" + this.errorCode + ": " + this.errorText;
	}
	
}
