import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class TCPApplication {

	private static final String[] OPTIONS = { "Server", "Client" };

	public static void main(String[] args) {
		String input = (String) JOptionPane.showInputDialog(null,
				"Soll die Server oder Client Anwendung gestartet werden?",
				"Start Chat", JOptionPane.QUESTION_MESSAGE, null, OPTIONS,
				OPTIONS[1]);
		if (OPTIONS[0].equals(input)) {
			int port = -1;
			while (port <= 0) {
				String res = JOptionPane.showInputDialog("Port:");
				try {
					port = Integer.parseInt(res);
				} catch (NumberFormatException e) {
				}
			}
			TCPServer server = new TCPServer(port, "Testserver");
			try {
				server.startServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (OPTIONS[1].equals(input)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					LoginDialog dialog = new LoginDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);

					Socket socket = dialog.getSocket();
					User user = dialog.getUser();
					if (socket == null || user == null) {
						return;
					}

					ServerConnection conn = new ServerConnection(socket, user);

					try {
						TCPClientWindow frame = new TCPClientWindow(conn);
						frame.setVisible(true);
						conn.startListenThread();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			System.exit(0);
		}
	}
}
