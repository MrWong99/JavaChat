import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UserManager {

	private String saveFileName;

	private ArrayList<User> users;

	public UserManager(String saveFileName) {
		this.saveFileName = saveFileName;
	}

	public boolean addUser(User user) {
		if (users.contains(user)) {
			return false;
		} else {
			users.add(user);
			return true;
		}
	}

	public boolean logout(User user) {
		if (users.contains(user)) {
			User fromList = users.get(users.indexOf(user));
			if (fromList.isOnline()
					&& fromList.checkPassword(user.getPassword())) {
				fromList.setOnline(false);
				return true;
			}
		}
		return false;
	}

	public boolean checkAndLogin(User user) {
		if (users.contains(user)) {
			User fromList = users.get(users.indexOf(user));
			if (!fromList.isOnline()
					&& fromList.checkPassword(user.getPassword())) {
				fromList.setOnline(true);
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void loadLogins() throws IOException {
		ObjectInputStream oin = new ObjectInputStream(new FileInputStream(
				saveFileName));
		try {
			users = (ArrayList<User>) oin.readObject();
			if (users == null) {
				users = new ArrayList<User>();
			}
		} catch (Exception e) {
			users = new ArrayList<User>();
		}
		oin.close();
	}

	public void saveLogins() throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(
				saveFileName));
		oout.writeObject(users);
		oout.flush();
		oout.close();
	}
}
