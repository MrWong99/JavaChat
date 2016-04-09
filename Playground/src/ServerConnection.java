import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ServerConnection {

	public long listenInterval = 500;

	private Socket socket;

	private User user;

	private ArrayList<StyledDocument> textDisplay = new ArrayList<StyledDocument>();

	private Thread listen;

	private ObjectOutputStream oout;

	private ArrayList<HashMap<String, Style>> displayStyles = new ArrayList<>();

	private CommandHandler handler;

	public ServerConnection(Socket socket, User user) {
		this.user = user;
		this.socket = socket;
	}

	public synchronized void addTextDisplay(StyledDocument display) {
		textDisplay.add(display);
		displayStyles.add(getStandardStyles(display));
	}

	public void startListenThread() throws IOException {
		if (listen == null) {
			final ObjectInputStream userInputStream = new ObjectInputStream(
					socket.getInputStream());
			oout = new ObjectOutputStream(socket.getOutputStream());
			listen = new Thread(new Runnable() {
				@Override
				public void run() {
					user.setOnline(true);
					while (!Thread.interrupted() && user.isOnline()
							&& socket.isConnected()) {
						try {
							Message input = (Message) userInputStream
									.readObject();
							displayInput(input);
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Thread.sleep(listenInterval);
						} catch (InterruptedException e) {
							break;
						}
					}
					user.setOnline(false);
				}
			});
			listen.start();
		}
	}

	public void stopListenThread() {
		if (listen != null && listen.isAlive()) {
			listen.interrupt();
			listen = null;
		}
		if (oout != null) {
			try {
				oout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			oout = null;
		}
	}

	public void sendMessage(Message message) throws IOException {
		if (oout != null) {
			oout.writeObject(message);
			oout.flush();
		}
	}

	public CommandHandler getHandler() {
		return handler;
	}

	public void setHandler(CommandHandler handler) {
		this.handler = handler;
	}

	private void displayInput(Message input) {
		if (input.getType() != null) {
			switch (input.getType()) {
			case CHATMESSAGE:
				displayInChat(input.getData());
				break;
			case ERRORREPORT:
				Object data = input.getData();
				if (data == null) {
					data = "Error.";
				}
				displayWithStyle(data, "errormessages");
				break;
			case SUCCESSREPORT:
				Object dat = input.getData();
				if (dat == null) {
					dat = "Success.";
				}
				displayWithStyle(dat, "successmessages");
				break;
			case COMMAND:
				handleCommand(input);
				break;
			default:
				break;
			}
		}
	}

	private void displayWithStyle(Object dat, String style) {
		for (int i = 0; i < textDisplay.size(); i++) {
			StyledDocument doc = textDisplay.get(i);
			try {
				doc.insertString(doc.getLength(), dat.toString() + "\n",
						displayStyles.get(i).get(style));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	private void displayInChat(Serializable data) {
		if (data != null) {
			if (data instanceof ChatMessage) {
				for (int i = 0; i < textDisplay.size(); i++) {
					StyledDocument doc = textDisplay.get(i);
					try {
						doc.insertString(doc.getLength(),
								((ChatMessage) data).getSenderName() + ": ",
								displayStyles.get(i).get("usernames"));
						doc.insertString(doc.getLength(), ((ChatMessage) data)
								.getMessageData().toString() + "\n",
								displayStyles.get(i).get("chatmessage"));
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			} else {
				for (int i = 0; i < textDisplay.size(); i++) {
					StyledDocument doc = textDisplay.get(i);
					try {
						doc.insertString(doc.getLength(), data.toString()
								+ "\n", displayStyles.get(i)
								.get("chatmessages"));
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void handleCommand(Message message) {
		try {
			Object data = message.getData();
			if (data != null && data instanceof ICommand) {
				if (handler != null) {
					Message result = handler.executeCommand((ICommand) data);
					sendMessage(result);
				} else {
					sendMessage(new Message(Message.MessageType.ERRORREPORT,
							"No handler active."));
				}
			} else {
				sendMessage(new Message(Message.MessageType.ERRORREPORT,
						"No command send."));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, Style> getStandardStyles(StyledDocument doc) {
		HashMap<String, Style> ret = new HashMap<>();

		String styleName = "usernames";
		Style style = doc.addStyle(styleName, null);
		StyleConstants.setForeground(style, Color.BLUE);
		ret.put(styleName, style);

		styleName = "chatmessages";
		style = doc.addStyle(styleName, null);
		StyleConstants.setForeground(style, Color.BLACK);
		ret.put(styleName, style);

		styleName = "errormessages";
		style = doc.addStyle(styleName, null);
		StyleConstants.setForeground(style, Color.RED);
		ret.put(styleName, style);

		styleName = "successmessages";
		style = doc.addStyle(styleName, null);
		StyleConstants.setForeground(style, Color.GREEN);
		ret.put(styleName, style);

		styleName = "commandmessages";
		style = doc.addStyle(styleName, null);
		StyleConstants.setForeground(style, Color.ORANGE);
		ret.put(styleName, style);

		return ret;
	}
}
