import java.io.Serializable;


public class ChatMessage implements Serializable {

	private static final long serialVersionUID = -50181201681235828L;
	
	private String senderName;
	
	private Serializable messageData;

	public ChatMessage(Serializable messageData) {
		this.messageData = messageData;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Serializable getMessageData() {
		return messageData;
	}

	public void setMessageData(Serializable messageData) {
		this.messageData = messageData;
	}
}
