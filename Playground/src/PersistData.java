import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class PersistData {
	public static void persist(String location, Serializable data)
			throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(
				location));
		oout.writeObject(data);
		oout.flush();
		oout.close();
	}

	public static Serializable load(String location)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream oin = new ObjectInputStream(new FileInputStream(
				location));
		Object data = oin.readObject();
		oin.close();
		if (data instanceof Serializable) {
			return (Serializable) data;
		} else {
			throw new ClassNotFoundException(
					"Loaded object not of type 'Serializable.'");
		}
	}
}
