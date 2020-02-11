package archibald.likes.packages.app;

import java.util.Arrays;

import archibald.likes.packages.api.commands.BotCommandInvocation;
import archibald.likes.packages.api.commands.BotCommandNamespace;
import archibald.likes.packages.api.commands.BotCommandNamespace.PublicCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SortingCommandNamespace extends BotCommandNamespace {
	{
		addCommandHelp("sort", "Sorts a list of Strings", "sort [args...]", "srt");
		new PublicCommand("sort", "srt") {
			@Override
			protected void run(BotCommandInvocation<MessageReceivedEvent> data) {
				if (data.args.length == 0) {
					reply(data, "You need to specify the items you want sorted");
				} else {
					String[] args = data.args;
					Arrays.sort(args);
					reply(data, "[" + String.join(", ", args) + "]");
				}
			}
		};
	}

}
