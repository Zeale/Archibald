package archibald.likes.packages.api.utils;

import java.util.ArrayList;
import java.util.List;

import archibald.likes.packages.api.discord.DiscordEventHandler;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class CombinedIListener<E extends Event> implements EventListener {

	private final Class<E> cls;

	private final List<DiscordEventHandler<E>> listeners = new ArrayList<>();

	public CombinedIListener(Class<E> eventType) {
		cls = eventType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(GenericEvent event) {
		if (cls.isInstance(event))
			for (final DiscordEventHandler<E> l : listeners)
				l.handle((E) event);
	}

	public void register(DiscordEventHandler<E> listener) {
		listeners.add(listener);
	}

	public void unregister(DiscordEventHandler<E> listener) {
		listeners.remove(listener);
	}
}
