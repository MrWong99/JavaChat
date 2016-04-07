import java.io.Serializable;

public class Message implements Serializable {

	public enum MessageType {
		COMMAND, CHATMESSAGE, ERRORREPORT, SUCCESSREPORT, LOGINREQUEST, ADDUSERREQUEST;
	}

	private static final long serialVersionUID = -2723363051271966964L;

	private MessageType type;

	private Serializable data;

	public Message(MessageType type, Serializable data) {
		this.type = type;
		this.data = data;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public Serializable getData() {
		return data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}
}
