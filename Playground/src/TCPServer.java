import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer {

	private UserManager userManager;

	private ServerSocket socket;

	private int port;

	private ArrayList<ClientConnection> connectedClients = new ArrayList<ClientConnection>();

	private CommandHandler commandHandler;

	private String servername;

	private Thread acceptThread;

	private TCPServer server = this;

	public TCPServer(int port, String servername) {
		this.port = port;
		this.servername = servername;
	}

	public void startServer() throws IOException {
		socket = new ServerSocket(port);
		userManager = new UserManager(servername + ".uman");
		commandHandler = new CommandHandler(new Object[] { userManager, this });
		startAcceptThread();
	}

	public boolean isAlive() {
		return acceptThread.isAlive();
	}

	public void broadcastMessage(Message message) {
		for (ClientConnection conn : connectedClients) {
			if (conn.connectionIsActive() && conn.getUser().isOnline()) {
				try {
					conn.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void startAcceptThread() {
		if (acceptThread == null) {
			acceptThread = new Thread(new Runnable() {
				@Override
				public void run() {
					ObjectInputStream oin;
					ObjectOutputStream oout;
					while (!Thread.interrupted() && !socket.isClosed()) {
						try {
							Socket clientSocket = socket.accept();
							oin = new ObjectInputStream(clientSocket
									.getInputStream());
							oout = new ObjectOutputStream(clientSocket
									.getOutputStream());
							Object loginRequest = oin.readObject();
							if (loginRequest != null
									&& loginRequest instanceof Message) {
								Message.MessageType type = ((Message) loginRequest)
										.getType();
								Object data = ((Message) loginRequest)
										.getData();
								if (type != null) {
									if (type == Message.MessageType.LOGINREQUEST) {
										if (data != null
												&& data instanceof User) {
											if (userManager
													.checkAndLogin((User) data)) {
												ClientConnection clientConn = new ClientConnection(
														server, clientSocket,
														(User) data,
														userManager);
												connectedClients
														.add(clientConn);
												clientConn.startConnection();
											} else {
												oout.writeObject(new Message(
														Message.MessageType.ERRORREPORT,
														"Login failed."));
												oout.flush();
											}

										} else {
											oout.writeObject(new Message(
													Message.MessageType.ERRORREPORT,
													"No user data given."));
											oout.flush();
										}
									} else if (type == Message.MessageType.ADDUSERREQUEST) {
										if (data != null
												&& data instanceof User) {
											if (userManager
													.addUser((User) data)) {
												oout.writeObject(new Message(
														Message.MessageType.SUCCESSREPORT,
														"User created."));
												oout.flush();
											} else {
												oout.writeObject(new Message(
														Message.MessageType.ERRORREPORT,
														"User already exists."));
												oout.flush();
											}
										} else {
											oout.writeObject(new Message(
													Message.MessageType.ERRORREPORT,
													"No user data given."));
											oout.flush();
										}
									} else {
										oout.writeObject(new Message(
												Message.MessageType.ERRORREPORT,
												"No login or add user request."));
										oout.flush();
									}
								} else {
									oout.writeObject(new Message(
											Message.MessageType.ERRORREPORT,
											"No messagetype given."));
									oout.flush();
								}
							} else {
								oout.writeObject(new Message(
										Message.MessageType.ERRORREPORT,
										"No message send."));
								oout.flush();
							}
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			});
			acceptThread.start();
		}
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}
}
