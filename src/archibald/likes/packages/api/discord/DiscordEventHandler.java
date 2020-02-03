package archibald.likes.packages.api.discord;

import net.dv8tion.jda.api.events.Event;

public interface DiscordEventHandler<E extends Event> {
	void handle(E event);
}
