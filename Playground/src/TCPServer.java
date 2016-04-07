import java.net.ServerSocket;

public class TCPServer {

	private UserManager userManager;

	private ServerSocket socket;

	private int port;

	public TCPServer(int port) {
		this.port = port;
	}
}
