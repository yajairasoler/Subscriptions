package ysm.ms.app.rest;

public class Message {

	private MessageType type;
	private String message;

	public Message() {

	}

	public Message(MessageType type, String message) {

		this.type = type;
		this.message = message;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
