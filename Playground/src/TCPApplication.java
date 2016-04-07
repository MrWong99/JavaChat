import java.awt.EventQueue;

import javax.swing.JOptionPane;

public class TCPApplication {

	private static final String[] OPTIONS = { "Server", "Client" };

	public static void main(String[] args) {
		String input = (String) JOptionPane.showInputDialog(null,
				"Soll die Server oder Client Anwendung gestartet werden?",
				"Start Chat", JOptionPane.QUESTION_MESSAGE, null, OPTIONS,
				OPTIONS[1]);
		if (OPTIONS[0].equals(input)) {

		} else if (OPTIONS[1].equals(input)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						TCPClientWindow frame = new TCPClientWindow();
						frame.setVisible(true);
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
