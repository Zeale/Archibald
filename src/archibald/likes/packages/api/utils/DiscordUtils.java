package archibald.likes.packages.api.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.PermissionUtil;

public final class DiscordUtils {
	public static boolean canAttachFile(MessageChannel channel) {
		return canPMOrHasGPerm(channel, Permission.MESSAGE_ATTACH_FILES);
	}

	public static boolean canPM(PrivateChannel pc) {
		return !pc.getUser().getMutualGuilds().isEmpty();
	}

	public static boolean canPMOrHasGPerm(MessageChannel channel, Permission perm) {
		return channel.getType() == ChannelType.PRIVATE ? canPM((PrivateChannel) channel)
				: hasGuildPerm((GuildChannel) channel, perm);
	}

	public static boolean canPMOrHasGPerms(MessageChannel channel, Permission... perms) {
		return channel.getType() == ChannelType.PRIVATE ? canPM((PrivateChannel) channel)
				: hasGuildPerms((GuildChannel) channel, perms);
	}

	public static boolean canSendMessage(MessageChannel channel) {
		return canPMOrHasGPerm(channel, Permission.MESSAGE_WRITE);
	}

	public static EmbedBuilder embedBuilderFromUser(User user) {
		return new EmbedBuilder().setAuthor(user.getName() + "#" + user.getDiscriminator(), null, user.getAvatarUrl());
	}

	public static String fixStringEncoding(String textFromDiscord) {
		return textFromDiscord;// new String(textFromDiscord.getBytes(), StandardCharsets.UTF_8);
	}

	public static EnumSet<Permission> getBotPerms(GuildChannel channel) {
		return Permission
				.getPermissions(PermissionUtil.getEffectivePermission(channel, channel.getGuild().getSelfMember()));
	}

	public static List<User> getUsersByName(String name, boolean ignoreCase, JDA client) {
		final List<User> users = new ArrayList<>(1);
		if (ignoreCase)
			for (final User u : client.getUsers()) {
				if (fixStringEncoding(u.getName()).equalsIgnoreCase(name))
					users.add(u);
			}
		else
			for (final User u : client.getUsers())
				if (fixStringEncoding(u.getName()).equals(name))
					users.add(u);
		return users;
	}

	public static boolean hasAnyGuildPerm(GuildChannel gc, Permission... perms) {
		final Collection<Permission> pcoll = getBotPerms(gc);
		for (final Permission p : perms)
			if (pcoll.contains(p))
				return true;
		return false;
	}

	public static boolean hasGuildPerm(GuildChannel gc, Permission perm) {
		return getBotPerms(gc).contains(perm);
	}

	public static boolean hasGuildPerms(GuildChannel gc, Permission... perms) {
		return Permission.getPermissions(PermissionUtil.getEffectivePermission(gc, gc.getGuild().getSelfMember()))
				.containsAll(Arrays.asList(perms));
	}

}
