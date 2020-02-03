package archibald.likes.packages.api.commands;

import org.alixia.javalibrary.commands.StringCommand;

public class BotCommandInvocation<S> extends StringCommand {

	public final S source;

	public final String namespaces[];

	public BotCommandInvocation(String command, String inputText, String[] namespaces, S source, String... args) {
		super(command, inputText, args);
		this.namespaces = namespaces;
		this.source = source;
	}

	public String cmd() {
		return getCommand();
	}

	public String[] getArgs() {
		return args;
	}

	public String getCmd() {
		return getCommand();
	}

	public String getCommand() {
		return command;
	}

	public S getData() {
		return source;
	}

	public String getInputText() {
		return inputText;
	}

	public String[] getNamespaces() {
		return namespaces;
	}

	public String input() {
		return inputText;
	}
}
