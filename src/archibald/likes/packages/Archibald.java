package archibald.likes.packages;

import java.util.Scanner;

import org.alixia.javalibrary.strings.matching.Matching;

import archibald.likes.packages.api.utils.CombinedIListener;
import archibald.likes.packages.app.PublicCommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

/**
 * The main class of the Archibald bot. Instances of this class store the state
 * of an entire instance of Archibald.
 * 
 * @author Zeale
 *
 */
public class Archibald {
	private final CombinedIListener<MessageReceivedEvent> messageHandler = new CombinedIListener<MessageReceivedEvent>(
			MessageReceivedEvent.class);

	private final JDA instance;

	public JDA getInstance() {
		return instance;
	}

	private final PublicCommandHandler commandHandler;

	public Archibald(String token) {
		commandHandler = new PublicCommandHandler(Matching.build(">", "+"), this);
		messageHandler.register(event -> commandHandler.run(event));
		JDABuilder builder = new JDABuilder(token);
		try {
			builder.addEventListeners((EventListener) event -> messageHandler.onEvent(event));
			instance = builder.build().awaitReady();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create an Archibald. :(");
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new Archibald(new Scanner(System.in).nextLine());
	}
}
