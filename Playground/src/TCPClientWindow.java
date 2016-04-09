import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.StyledDocument;

public class TCPClientWindow extends JFrame {

	private static final long serialVersionUID = 2370283357198188679L;

	private JPanel contentPane;

	private JList<String> userList;

	private JTextField textInput;

	private StyledDocument chatDisplayDoc;

	private ServerConnection connection;

	public TCPClientWindow(ServerConnection connection) {
		super("Chat Client");

		this.connection = connection;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JScrollPane chatScrollPane = new JScrollPane();
		chatScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_chatScrollPane = new GridBagConstraints();
		gbc_chatScrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_chatScrollPane.fill = GridBagConstraints.BOTH;
		gbc_chatScrollPane.gridx = 0;
		gbc_chatScrollPane.gridy = 0;
		contentPane.add(chatScrollPane, gbc_chatScrollPane);

		JTextPane chatDisplay = new JTextPane();
		chatDisplay.setEditable(false);
		chatDisplay.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
		chatDisplayDoc = chatDisplay.getStyledDocument();
		connection.addTextDisplay(chatDisplayDoc);
		chatScrollPane.setViewportView(chatDisplay);

		JScrollPane listScrollPane = new JScrollPane();
		listScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_listScrollPane = new GridBagConstraints();
		gbc_listScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_listScrollPane.fill = GridBagConstraints.BOTH;
		gbc_listScrollPane.gridx = 1;
		gbc_listScrollPane.gridy = 0;
		contentPane.add(listScrollPane, gbc_listScrollPane);

		userList = new JList<String>();
		userList.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
		listScrollPane.setViewportView(userList);

		ActionListener sendListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendInput();
			}
		};

		textInput = new JTextField();
		textInput.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
		GridBagConstraints gbc_textInput = new GridBagConstraints();
		gbc_textInput.insets = new Insets(0, 0, 0, 5);
		gbc_textInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_textInput.gridx = 0;
		gbc_textInput.gridy = 1;
		textInput.addActionListener(sendListener);
		contentPane.add(textInput, gbc_textInput);
		textInput.setColumns(10);

		JButton btnSend = new JButton("Senden");
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.fill = GridBagConstraints.VERTICAL;
		gbc_btnSend.anchor = GridBagConstraints.WEST;
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 1;
		btnSend.addActionListener(sendListener);
		contentPane.add(btnSend, gbc_btnSend);
	}

	private void sendInput() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					connection.sendMessage(new Message(
							Message.MessageType.CHATMESSAGE, textInput
									.getText()));
				} catch (IOException e) {
					JOptionPane.showMessageDialog(
							null,
							"Senden der Nachricht fehlgeschlagen:\n"
									+ e.getMessage(), "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
