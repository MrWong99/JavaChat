import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginDialog extends JDialog {

	private static final long serialVersionUID = -5372463753157935156L;

	private final JPanel contentPanel = new JPanel();

	private JTextField txtIp;

	private JTextField txtPort;

	private JLabel dispConnectivity;

	private JCheckBox chkSaveUserData;

	private JCheckBox chkSaveConnData;

	private JTextField txtUsername;

	private JPasswordField txtPassword;

	private User user;

	private Socket socket;

	public LoginDialog() {
		setBounds(100, 100, 555, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, 1.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel connectionPanel = new JPanel();
			connectionPanel.setBackground(Color.LIGHT_GRAY);
			GridBagConstraints gbc_connectionPanel = new GridBagConstraints();
			gbc_connectionPanel.insets = new Insets(0, 0, 0, 5);
			gbc_connectionPanel.fill = GridBagConstraints.BOTH;
			gbc_connectionPanel.gridx = 0;
			gbc_connectionPanel.gridy = 0;
			contentPanel.add(connectionPanel, gbc_connectionPanel);
			GridBagLayout gbl_connectionPanel = new GridBagLayout();
			gbl_connectionPanel.columnWidths = new int[] { 0, 30, 30 };
			gbl_connectionPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0 };
			gbl_connectionPanel.columnWeights = new double[] { 1.0, 0.0,
					Double.MIN_VALUE };
			gbl_connectionPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			connectionPanel.setLayout(gbl_connectionPanel);
			{
				JLabel lblServerSettings = new JLabel("Server Einstellungen:");
				lblServerSettings.setForeground(Color.BLUE);
				GridBagConstraints gbc_lblServerSettings = new GridBagConstraints();
				gbc_lblServerSettings.gridwidth = 3;
				gbc_lblServerSettings.insets = new Insets(0, 0, 5, 0);
				gbc_lblServerSettings.gridx = 0;
				gbc_lblServerSettings.gridy = 0;
				connectionPanel.add(lblServerSettings, gbc_lblServerSettings);
			}
			{
				JLabel lblAddress = new JLabel("IP-Addresse:");
				GridBagConstraints gbc_lblAddress = new GridBagConstraints();
				gbc_lblAddress.gridwidth = 3;
				gbc_lblAddress.insets = new Insets(0, 0, 5, 0);
				gbc_lblAddress.anchor = GridBagConstraints.WEST;
				gbc_lblAddress.gridx = 0;
				gbc_lblAddress.gridy = 2;
				connectionPanel.add(lblAddress, gbc_lblAddress);
			}
			{
				txtIp = new JTextField();
				GridBagConstraints gbc_txtIp = new GridBagConstraints();
				gbc_txtIp.gridwidth = 2;
				gbc_txtIp.insets = new Insets(0, 0, 5, 0);
				gbc_txtIp.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtIp.gridx = 0;
				gbc_txtIp.gridy = 3;
				connectionPanel.add(txtIp, gbc_txtIp);
				txtIp.setColumns(10);
			}
			{
				JLabel lblPort = new JLabel("Port:");
				GridBagConstraints gbc_lblPort = new GridBagConstraints();
				gbc_lblPort.gridwidth = 3;
				gbc_lblPort.insets = new Insets(0, 0, 5, 0);
				gbc_lblPort.anchor = GridBagConstraints.WEST;
				gbc_lblPort.gridx = 0;
				gbc_lblPort.gridy = 5;
				connectionPanel.add(lblPort, gbc_lblPort);
			}
			{
				txtPort = new JTextField();
				GridBagConstraints gbc_txtPort = new GridBagConstraints();
				gbc_txtPort.insets = new Insets(0, 0, 5, 0);
				gbc_txtPort.gridwidth = 2;
				gbc_txtPort.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtPort.gridx = 0;
				gbc_txtPort.gridy = 6;
				connectionPanel.add(txtPort, gbc_txtPort);
				txtPort.setColumns(10);
			}
			{
				dispConnectivity = new JLabel("");
				dispConnectivity.setForeground(Color.BLACK);
				GridBagConstraints gbc_dispConnectivity = new GridBagConstraints();
				gbc_dispConnectivity.anchor = GridBagConstraints.EAST;
				gbc_dispConnectivity.insets = new Insets(0, 0, 5, 5);
				gbc_dispConnectivity.gridx = 0;
				gbc_dispConnectivity.gridy = 7;
				connectionPanel.add(dispConnectivity, gbc_dispConnectivity);
			}
			{
				JButton btnCheckConnection = new JButton("Testen");
				GridBagConstraints gbc_btnCheckConnection = new GridBagConstraints();
				gbc_btnCheckConnection.insets = new Insets(0, 0, 5, 0);
				gbc_btnCheckConnection.gridx = 1;
				gbc_btnCheckConnection.gridy = 7;
				btnCheckConnection.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						checkAndDisplayConnection();
					}
				});
				connectionPanel.add(btnCheckConnection, gbc_btnCheckConnection);
			}
			{
				chkSaveConnData = new JCheckBox("Daten speichern");
				chkSaveConnData.setBackground(Color.LIGHT_GRAY);
				GridBagConstraints gbc_chkSaveConnData = new GridBagConstraints();
				gbc_chkSaveConnData.insets = new Insets(0, 0, 0, 5);
				gbc_chkSaveConnData.gridx = 0;
				gbc_chkSaveConnData.gridy = 8;
				connectionPanel.add(chkSaveConnData, gbc_chkSaveConnData);
			}
		}
		{
			JPanel userPanel = new JPanel();
			userPanel.setBackground(Color.LIGHT_GRAY);
			GridBagConstraints gbc_userPanel = new GridBagConstraints();
			gbc_userPanel.fill = GridBagConstraints.BOTH;
			gbc_userPanel.gridx = 1;
			gbc_userPanel.gridy = 0;
			contentPanel.add(userPanel, gbc_userPanel);
			GridBagLayout gbl_userPanel = new GridBagLayout();
			gbl_userPanel.columnWidths = new int[] { 0, 0, 0 };
			gbl_userPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			gbl_userPanel.columnWeights = new double[] { 1.0, 0.0,
					Double.MIN_VALUE };
			gbl_userPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			userPanel.setLayout(gbl_userPanel);
			{
				JLabel lblBenutzerEinstellungen = new JLabel(
						"Benutzer Einstellungen:");
				lblBenutzerEinstellungen.setForeground(Color.BLUE);
				GridBagConstraints gbc_lblBenutzerEinstellungen = new GridBagConstraints();
				gbc_lblBenutzerEinstellungen.gridwidth = 2;
				gbc_lblBenutzerEinstellungen.insets = new Insets(0, 0, 5, 0);
				gbc_lblBenutzerEinstellungen.gridx = 0;
				gbc_lblBenutzerEinstellungen.gridy = 0;
				userPanel.add(lblBenutzerEinstellungen,
						gbc_lblBenutzerEinstellungen);
			}
			{
				JLabel lblUsername = new JLabel("Benutzername:");
				GridBagConstraints gbc_lblUsername = new GridBagConstraints();
				gbc_lblUsername.gridwidth = 2;
				gbc_lblUsername.anchor = GridBagConstraints.WEST;
				gbc_lblUsername.insets = new Insets(0, 0, 5, 0);
				gbc_lblUsername.gridx = 0;
				gbc_lblUsername.gridy = 2;
				userPanel.add(lblUsername, gbc_lblUsername);
			}
			{
				txtUsername = new JTextField();
				GridBagConstraints gbc_txtUsername = new GridBagConstraints();
				gbc_txtUsername.gridwidth = 2;
				gbc_txtUsername.insets = new Insets(0, 0, 5, 0);
				gbc_txtUsername.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtUsername.gridx = 0;
				gbc_txtUsername.gridy = 3;
				userPanel.add(txtUsername, gbc_txtUsername);
				txtUsername.setColumns(10);
			}
			{
				JLabel lblPassword = new JLabel("Passwort:");
				GridBagConstraints gbc_lblPassword = new GridBagConstraints();
				gbc_lblPassword.anchor = GridBagConstraints.WEST;
				gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
				gbc_lblPassword.gridx = 0;
				gbc_lblPassword.gridy = 5;
				userPanel.add(lblPassword, gbc_lblPassword);
			}
			{
				txtPassword = new JPasswordField();
				GridBagConstraints gbc_txtPassword = new GridBagConstraints();
				gbc_txtPassword.gridwidth = 2;
				gbc_txtPassword.insets = new Insets(0, 0, 5, 5);
				gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtPassword.gridx = 0;
				gbc_txtPassword.gridy = 6;
				userPanel.add(txtPassword, gbc_txtPassword);
			}
			{
				JButton btnBenutzerErstellen = new JButton("Benutzer erstellen");
				GridBagConstraints gbc_btnBenutzerErstellen = new GridBagConstraints();
				gbc_btnBenutzerErstellen.insets = new Insets(0, 0, 5, 0);
				gbc_btnBenutzerErstellen.gridx = 1;
				gbc_btnBenutzerErstellen.gridy = 7;
				btnBenutzerErstellen.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (socket != null && !socket.isClosed()) {
							try {
								ObjectOutputStream oout = new ObjectOutputStream(
										socket.getOutputStream());
								ObjectInputStream oin = new ObjectInputStream(
										socket.getInputStream());
								oout.writeObject(new Message(
										Message.MessageType.ADDUSERREQUEST,
										new User(txtUsername.getText(),
												new String(txtPassword
														.getPassword())
														.hashCode())));
								oout.flush();
								Object in = oin.readObject();
								if (in != null && in instanceof Message
										&& ((Message) in).getType() != null) {
									switch (((Message) in).getType()) {
									case SUCCESSREPORT:
										JOptionPane.showMessageDialog(null,
												"Benutzer erstellt.");
										break;
									case ERRORREPORT:
										user = null;
										JOptionPane.showMessageDialog(null,
												((Message) in).getData(),
												"Fehler",
												JOptionPane.ERROR_MESSAGE);
										break;
									default:
										break;
									}
								}
							} catch (IOException | ClassNotFoundException e1) {
								displayNoConnection();
							}
						} else {
							displayNoConnection();
						}
					}

					private void displayNoConnection() {
						JOptionPane
								.showMessageDialog(
										null,
										"Es besteht keine Serververbindung. Richten sie diese zunaechst ein und 'Testen' Sie sie.",
										"Fehler", JOptionPane.ERROR_MESSAGE);
					}
				});
				userPanel.add(btnBenutzerErstellen, gbc_btnBenutzerErstellen);
			}
			{
				chkSaveUserData = new JCheckBox("Daten speichern");
				chkSaveUserData.setBackground(Color.LIGHT_GRAY);
				GridBagConstraints gbc_chkSaveUserData = new GridBagConstraints();
				gbc_chkSaveUserData.insets = new Insets(0, 0, 0, 5);
				gbc_chkSaveUserData.gridx = 0;
				gbc_chkSaveUserData.gridy = 8;
				userPanel.add(chkSaveUserData, gbc_chkSaveUserData);
			}
		}
		{
			ActionListener disposeListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			};
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton loginBtn = new JButton("Login");
				loginBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (socket == null || !socket.isConnected()) {
							checkAndDisplayConnection();
						}
						if (socket != null) {
							user = new User(txtUsername.getText(), new String(
									txtPassword.getPassword()).hashCode());
							try {
								ObjectOutputStream oout = new ObjectOutputStream(
										socket.getOutputStream());
								ObjectInputStream oin = new ObjectInputStream(
										socket.getInputStream());
								oout.writeObject(new Message(
										Message.MessageType.LOGINREQUEST, user));
								oout.flush();
								Object in = oin.readObject();
								if (in != null && in instanceof Message
										&& ((Message) in).getType() != null) {
									switch (((Message) in).getType()) {
									case SUCCESSREPORT:
										dispose();
										break;
									case ERRORREPORT:
										user = null;
										JOptionPane.showMessageDialog(null,
												((Message) in).getData(),
												"Fehler",
												JOptionPane.ERROR_MESSAGE);
										break;
									default:
										break;
									}
								}
							} catch (IOException | ClassNotFoundException e1) {
								if (socket != null) {
									if (!socket.isClosed()) {
										try {
											socket.close();
										} catch (IOException e2) {
											e2.printStackTrace();
										}
									}
									socket = null;
								}
								if (user != null) {
									user = null;
								}
							}
						}
					}
				});

				buttonPane.add(loginBtn);
				getRootPane().setDefaultButton(loginBtn);
			}
			{
				JButton cancelBtn = new JButton("Abbrechen");
				cancelBtn.addActionListener(disposeListener);
				buttonPane.add(cancelBtn);
			}
		}

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (chkSaveConnData.isSelected()) {
					try {
						PersistData.persist("server.ip", txtIp.getText());
						PersistData.persist("server.port", txtPort.getText());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (chkSaveUserData.isSelected()) {
					try {
						PersistData.persist("user.name", txtUsername.getText());
						PersistData.persist("user.pass",
								txtPassword.getPassword());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		loadSavedData();
	}

	public User getUser() {
		return user;
	}

	public Socket getSocket() {
		return socket;
	}

	private void checkAndDisplayConnection() {
		try {
			Socket fromSetting = new Socket(txtIp.getText(),
					Integer.parseInt(txtPort.getText()));
			if (fromSetting.isConnected()) {
				if (socket != null) {
					if (!socket.isClosed()) {
						socket.close();
					}
					socket = null;
				}
				socket = fromSetting;
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						dispConnectivity.setForeground(Color.GREEN);
						dispConnectivity.setText("Verbindung aufgebaut");
					}
				});
			} else {
				System.out.println("No connected.");
			}
		} catch (Exception e) {
			if (socket != null) {
				if (!socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				socket = null;
			}
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					dispConnectivity.setForeground(Color.RED);
					dispConnectivity.setText("Keine Verbindung");
				}
			});
			e.printStackTrace();
		}
	}

	private void loadSavedData() {
		try {
			final Object dataIp = PersistData.load("server.ip");
			final Object dataPort = PersistData.load("server.port");
			if (dataIp != null && dataIp instanceof String && dataPort != null
					&& dataPort instanceof Integer) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						txtIp.setText((String) dataIp);
						txtPort.setText("" + dataPort);
					}
				});
			}
		} catch (ClassNotFoundException | IOException e) {
		}
		try {
			final Object dataName = PersistData.load("user.name");
			final Object dataPass = PersistData.load("user.pass");
			if (dataName != null && dataName instanceof String
					&& dataPass != null && dataPass instanceof String) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						txtUsername.setText((String) dataName);
						txtPassword.setText((String) dataPass);
					}
				});
			}
		} catch (ClassNotFoundException | IOException e) {
		}
	}
}
