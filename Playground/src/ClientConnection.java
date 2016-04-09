import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {

	private Socket socket;

	private ObjectOutputStream oout;

	private User user;

	private Thread listenThread;

	private UserManager userManager;

	private TCPServer server;

	public long listenInterval = 500;

	public ClientConnection(TCPServer server, Socket socket, User user,
			UserManager userManager) {
		this.socket = socket;
		this.user = user;
		this.userManager = userManager;
		this.server = server;
	}

	public User getUser() {
		return user;
	}

	public void startConnection() throws IOException {
		if (listenThread == null) {
			final ObjectInputStream userInputStream = new ObjectInputStream(
					socket.getInputStream());
			oout = new ObjectOutputStream(socket.getOutputStream());
			listenThread = new Thread(new Runnable() {
				@Override
				public void run() {
					user.setOnline(true);
					try {
						sendMessage(new Message(
								Message.MessageType.SUCCESSREPORT,
								"Login successful."));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					while (!Thread.interrupted() && user.isOnline()
							&& socket.isConnected()) {
						try {
							Message input = (Message) userInputStream
									.readObject();
							executeInput(input);
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
		}
	}

	public void stopConnection() {
		if (listenThread != null && listenThread.isAlive()) {
			listenThread.interrupt();
			listenThread = null;
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

	public void executeInput(Message message) {
		if (message.getType() == null) {
			try {
				sendMessage(new Message(Message.MessageType.ERRORREPORT,
						"No message type specified"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		switch (message.getType()) {
		case ADDUSERREQUEST:
			addUser(message);
			break;
		case LOGINREQUEST:
			changeLogin(message);
			break;
		case CHATMESSAGE:
			Object data = message.getData();
			if (data instanceof ChatMessage) {
				((ChatMessage) data).setSenderName(user.getUsername());
				server.broadcastMessage(message);
			} else {
				try {
					sendMessage(new Message(Message.MessageType.ERRORREPORT,
							"No message data send."));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		case COMMAND:
			handleCommand(message);
			break;
		case ERRORREPORT:
			System.err.println(message.getData());
			break;
		case SUCCESSREPORT:
			System.out.println(message.getData());
			break;
		default:
			break;
		}
	}

	private void addUser(Message message) {
		try {
			Object data = message.getData();
			if (data != null && data instanceof User) {
				if (userManager.addUser((User) message.getData())) {
					sendMessage(new Message(Message.MessageType.SUCCESSREPORT,
							"User created."));
				} else {
					sendMessage(new Message(Message.MessageType.ERRORREPORT,
							"User already exists."));
				}
			} else {
				sendMessage(new Message(Message.MessageType.ERRORREPORT,
						"No user data send."));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void changeLogin(Message message) {
		try {
			Object data = message.getData();
			if (data != null && data instanceof User) {
				if (userManager.checkAndLogin((User) data)) {
					user.setOnline(false);
					user = (User) data;
					sendMessage(new Message(Message.MessageType.SUCCESSREPORT,
							"User logged in."));
				} else {
					sendMessage(new Message(Message.MessageType.ERRORREPORT,
							"User could not be logged in."));
				}
			} else {
				sendMessage(new Message(Message.MessageType.ERRORREPORT,
						"No user data send."));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleCommand(Message message) {
		try {
			Object data = message.getData();
			if (data != null && data instanceof ICommand) {
				CommandHandler handler = server.getCommandHandler();
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

	public void sendMessage(Message message) throws IOException {
		if (oout != null) {
			oout.writeObject(message);
			oout.flush();
		}
	}

	public boolean connectionIsActive() {
		return listenThread != null && listenThread.isAlive();
	}
}
