package archibald.likes.packages.app;

import archibald.likes.packages.api.commands.BotCommandInvocation;
import archibald.likes.packages.api.commands.BotCommandNamespace;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SortingCommandNamespace extends BotCommandNamespace {
	{

		makeHelpCommand();

		PublicCommand cmd = new PublicCommand("algorithms", "algo") {
			String[] listOfAlgos = { "Name          ->\t[command]", "-----------------------------",
					"Bubble Sort   ->\t[-bbs]", "Bogo Sort     ->\t[-bgs]" };

			@Override
			protected void run(BotCommandInvocation<MessageReceivedEvent> data) {
				StringBuilder replyString = new StringBuilder("```");
				for (String s : listOfAlgos)
					replyString.append(s).append('\n');
				replyString.append("```");
				reply(data, replyString.toString());
			}
		};

		addCommandHelp("sort",
				"Sorts a list of based off a given sorting algorithm.\nType `+sort:algorithms` for more information",
				"sort [args...]", "srt");
		new PublicCommand("sort", "srt") {
			@Override
			protected void run(BotCommandInvocation<MessageReceivedEvent> data) {
				if (data.args.length == 0) {
					reply(data, "You need to specify the items you want sorted");
				} else {
					String[] args = data.args;
					long startTime = 0;
					long endTime = 0;
					long elapsedTime = 0;
					switch (args[0]) {
					case "-bbs":
						startTime = System.nanoTime();
						args = bubbleSort(args);
						endTime = System.nanoTime();
						break;
					default:
						reply(data, "Type of sort not specified");
						cmd.act(data);
						break;
					}
					elapsedTime = endTime - startTime;
					if (elapsedTime != 0)
						reply(data, "Result -> [" + String.join(", ", args) + "]" + "\n" + "Sort took " + elapsedTime
								+ " Nanoseconds");
				}
			}
		};

		addCommandHelp("algorithms", "Displays the list of algorithms currently supported", "algorithms", "algo");

	}

	// Algorithms
	public static <T extends Comparable<T>> T[] bubbleSort(T[] array) {
		int n = array.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (array[j].compareTo(array[j + 1]) > 0) {
					swap(array, j, j + 1);
				}
			}
		}
		return array;
	}

	public static void swap(Object[] array, int index1, int index2) {
		Object temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}

}
