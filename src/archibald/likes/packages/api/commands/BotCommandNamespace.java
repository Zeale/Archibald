package archibald.likes.packages.api.commands;

import static archibald.likes.packages.api.utils.DiscordUtils.canSendMessage;

import org.alixia.javalibrary.commands.GenericCommand;
import org.alixia.javalibrary.strings.matching.Matching;

import archibald.likes.packages.api.utils.DiscordUtils;
import archibald.likes.packages.app.PublicCommandHelpBook;
import archibald.likes.packages.app.PublicCommandHelpBook.CommandHelp;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BotCommandNamespace extends CommandNamespace<BotCommandInvocation<MessageReceivedEvent>, String> {

	public abstract static class ContextRequirement {

		public static final ContextRequirement DIRECT_MESSGE_CHANNEL = new ContextRequirement(
				"Must be in private messages") {

			@Override
			protected boolean passed(BotCommandInvocation<MessageReceivedEvent> cmd) {
				return cmd.getData().isFromType(ChannelType.PRIVATE);
			}
		}, GUILD_CHANNEL = new ContextRequirement("Must be in a server") {

			@Override
			protected boolean passed(BotCommandInvocation<MessageReceivedEvent> cmd) {
				return cmd.getData().isFromGuild();
			}

		};

		public static ContextRequirement[] compound(ContextRequirement... requirements) {
			return requirements;
		}

		public static String handle(BotCommandInvocation<MessageReceivedEvent> cmd,
				ContextRequirement... requirements) {
			for (int i = 0; i < requirements.length; i++)
				if (!requirements[i].passed(cmd)) {
					final StringBuilder err = new StringBuilder(
							"Requirements for this command that were not met: ```\n");
					err.append(requirements[i].requirement);
					for (i++; i < requirements.length; i++)
						if (!requirements[i].passed(cmd)) {
							err.append(requirements[i].requirement);
							err.append('\n');
						}
					err.append("```");
					return err.toString();
				}
			return null;
		}

		private final String requirement;

		public ContextRequirement(String requirement) {
			this.requirement = requirement;
		}

		protected abstract boolean passed(BotCommandInvocation<MessageReceivedEvent> cmd);

	}

	public abstract class PublicCommand implements GenericCommand<BotCommandInvocation<MessageReceivedEvent>> {

		private final Matching matching;
		private final ContextRequirement[] requirements;

		{
			addCommand(this);
		}

		public PublicCommand(boolean ignoreCase, String... cmds) {
			this(ignoreCase ? Matching.ignoreCase(cmds) : Matching.build(cmds));
		}

		public PublicCommand(ContextRequirement[] requirements, boolean ignoreCase, String... cmds) {
			this(ignoreCase ? Matching.ignoreCase(cmds) : Matching.build(cmds), requirements);
		}

		public PublicCommand(ContextRequirement[] requirements, String... cmds) {
			this(requirements, true, cmds);
		}

		public PublicCommand(Matching matching) {
			this(matching, new ContextRequirement[0]);
		}

		public PublicCommand(Matching matching, ContextRequirement... requirements) {
			this.matching = matching;
			this.requirements = requirements;
		}

		public PublicCommand(String... cmds) {
			this(true, cmds);
		}

		@Override
		public final void act(BotCommandInvocation<MessageReceivedEvent> data) {
			final String err = ContextRequirement.handle(data, requirements);
			if (err == null)
				run(data);
			else
				reply(data.getData(), err);
		}

		@Override
		public boolean match(BotCommandInvocation<MessageReceivedEvent> data) {
			return matching.fullyMatches(data.cmd());
		}

		protected abstract void run(BotCommandInvocation<MessageReceivedEvent> data);
	}

	private final PublicCommandHelpBook helpBook = new PublicCommandHelpBook();

	public void addCommandHelp(CommandHelp help) {
		helpBook.addCommand(help);
	}

	public CommandHelp addCommandHelp(String name, String description, String usage, String... aliases) {
		return helpBook.addCommand(name, description, usage, aliases);
	}

	public PublicCommandHelpBook getHelpBook() {
		return helpBook;
	}

	/**
	 * Creates and returns a {@link PublicCommand} that prints the help for this
	 * {@link BotCommandNamespace}. The {@link PublicCommand} is automatically
	 * registered to this {@link BotCommandNamespace}. The returned object will
	 * handle the <code>?</code> command and the <code>help</code> command for
	 * commands invoked on this namespace, ignoring capitalization. Additionally,
	 * this method will add the help command it creates to the help book of this
	 * {@link BotCommandNamespace}.
	 *
	 * @return A {@link PublicCommand} that handles help for this namespace.
	 */
	public PublicCommand makeHelpCommand() {
		final CommandHelp helpCommandHelp = addCommandHelp("help", "Shows help for commands.",
				"help [page-number|command-name]", "?");
		return new PublicCommand("help", "?") {

			@Override
			protected void run(BotCommandInvocation<MessageReceivedEvent> data) {
				if (!DiscordUtils.canSendMessage(data.getData().getChannel()))
					return;
				int page = 1;
				FIRST_ARG: if (data.getArgs().length == 1) {
					String arg;
					if (!data.getArgs()[0].startsWith("\\"))
						try {
							page = Integer.parseInt(data.getArgs()[0]);
							break FIRST_ARG;
						} catch (final NumberFormatException e) {
							arg = data.getArgs()[0];
						}
					else
						arg = data.getArgs()[0].substring(1);
					if (!printHelp(data.getData().getChannel(), arg, true, true))
						data.getData().getChannel()
								.sendMessage("No command with the name or alias: \"" + arg + "\" was found.").queue();
					return;
				} else if (data.getArgs().length > 1) {
					data.getData().getChannel().sendMessage("Illegal number of arguments for command: " + data.cmd())
							.queue();
					printHelp(data.getData().getChannel(), helpCommandHelp);
					return;
				}
				printHelp(data.getData().getChannel(), page);
			}
		};
	}

	public void printHelp(EmbedBuilder builder, CommandHelp help) {
		helpBook.print(builder, help);
	}

	public void printHelp(MessageChannel channel, CommandHelp help) {
		helpBook.print(channel, help);
	}

	public void printHelp(MessageChannel channel, int page) {
		helpBook.print(channel, page);
	}

	public boolean printHelp(MessageChannel channel, String command, boolean allowAliases, boolean ignoreCase) {
		return helpBook.print(channel, command, allowAliases, ignoreCase);
	}

	/**
	 * Sends the specified message in the channel that the
	 * {@link MessageReceivedEvent} happened in.
	 *
	 * @param event the {@link MessageReceivedEvent}.
	 * @param text  the reply.
	 */
	protected static final boolean reply(MessageReceivedEvent event, String text) {
		if (!canSendMessage(event.getChannel()))
			return false;
		event.getChannel().sendMessage(text).queue();
		return true;
	}

	/**
	 * Sends the specified text in the channel specified in the <code>data</code>'s
	 * {@link MessageReceivedEvent}, if possible. If this method fails due to a lack
	 * of permissions to send messages in a {@link GuildChannel}, or fails due to
	 * sharing no guilds with the recipient of a private channel, it returns
	 * <code>false</code>. Otherwise, it returns <code>true</code> (assuming it
	 * completes normally).
	 *
	 * @param data The data containing the {@link MessageReceivedEvent} containing
	 *             the {@link MessageChannel} to send messages in.
	 * @param text The text to send.
	 * @return <code>false</code> due to a lack of perms in a {@link GuildChannel}
	 *         or due to inability to send in a private message, or
	 *         <code>true</code> otherwise.
	 */
	protected final static boolean reply(BotCommandInvocation<MessageReceivedEvent> data, String text) {
		return reply(data.getData(), text);
	}

}
