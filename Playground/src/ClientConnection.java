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

	public long listenInterval = 500;

	public ClientConnection(Socket socket, User user, UserManager userManager) {
		this.socket = socket;
		this.user = user;
		this.userManager = userManager;
	}

	public User getUser() {
		return user;
	}

	public void startConnection() throws IOException {
		if (listenThread == null && !listenThread.isAlive()) {
			final ObjectInputStream userInputStream = new ObjectInputStream(
					socket.getInputStream());
			oout = new ObjectOutputStream(socket.getOutputStream());
			listenThread = new Thread(new Runnable() {
				@Override
				public void run() {
					user.setOnline(true);
					while (!Thread.interrupted() && user.isOnline()) {
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
		switch (message.getType()) {
		case ADDUSERREQUEST:
			try {
				Object data = message.getData();
				if (data != null && data instanceof User) {
					if (userManager.addUser((User) message.getData())) {
						sendMessage(new Message(
								Message.MessageType.SUCCESSREPORT,
								"User created."));
					} else {
						sendMessage(new Message(
								Message.MessageType.ERRORREPORT,
								"User already exists."));
					}
				} else {
					sendMessage(new Message(Message.MessageType.ERRORREPORT,
							"No user data send."));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	public void sendMessage(Message message) throws IOException {
		if (oout != null) {
			oout.writeObject(message);
			oout.flush();
		}
	}
}
