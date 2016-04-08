import java.util.HashMap;

public class CommandHandler {

	private HashMap<String, Object> insertables = new HashMap<String, Object>();

	public CommandHandler(Object[] insertables) {
		if (insertables != null) {
			for (Object o : insertables) {
				this.insertables.put(o.getClass().getName(), o);
			}
		}
	}

	public Message executeCommand(ICommand command) {
		Class<Object>[] parameters = command.getRequestParameters();
		Object[] inserted = null;
		if (parameters != null && parameters.length > 0) {
			inserted = new Object[parameters.length];
			int i = 0;
			for (Class<Object> parameter : parameters) {
				inserted[i] = insertables.get(parameter.getName());
				i++;
			}
		}
		return command.runCommand(inserted);
	}

	public boolean addInsertable(Object insertable) {
		if (insertable == null || insertables.containsValue(insertable)) {
			return false;
		}
		insertables.put(insertable.getClass().getName(), insertable);
		return true;
	}

	public boolean removeInsertable(Object insertable) {
		return insertables.remove(insertable.getClass().getName(), insertable);
	}
}
