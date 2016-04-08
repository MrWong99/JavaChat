import java.io.Serializable;

public interface ICommand extends Serializable {
	Class<Object>[] getRequestParameters();

	Message runCommand(Object[] arguments);
}
