import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class UserManager {

	private String saveFileName;

	private ArrayList<User> users = new ArrayList<>();

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
		try {
			Serializable users = PersistData.load(saveFileName);
			this.users = (ArrayList<User>) users;
		} catch (Exception e) {
			users = new ArrayList<>();
		}
	}

	public void saveLogins() throws IOException {
		PersistData.persist(saveFileName, users);
	}
}
