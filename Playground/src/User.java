import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -7633396410301071928L;

	private String username;

	private int hashedPassword;

	private boolean isOnline = false;

	public User(String username, int hashedPassword) {
		this.username = username;
		this.hashedPassword = hashedPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public int getPassword() {
		return hashedPassword;
	}

	public boolean checkPassword(int hashedPassword) {
		return this.hashedPassword == hashedPassword;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			return ((User) obj).username.equals(username);
		} else if (obj instanceof String) {
			return username.equals(obj);
		}
		return false;
	}

	@Override
	public String toString() {
		return "Username: " + username;
	}
}
