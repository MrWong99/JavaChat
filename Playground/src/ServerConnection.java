import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.text.StyledDocument;

public class ServerConnection {

	private Socket socket;

	private User user;

	private ArrayList<StyledDocument> textDisplay = new ArrayList<StyledDocument>();

	private Thread listen;

	private ObjectOutputStream oout;

	public ServerConnection(Socket socket, User user) {
		this.user = user;
		this.socket = socket;
	}

	public void addTextDisplay(StyledDocument display) {
		textDisplay.add(display);
	}

	public void startListenThread() {
		if (listen == null) {
			listen = new Thread(new Runnable() {

				@Override
				public void run() {

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
}
