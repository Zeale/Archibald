package archibald.likes.packages;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

/**
 * The main class of the Archibald bot. Instances of this class store the state
 * of an entire instance of Archibald.
 * 
 * @author Zeale
 *
 */
public class Archibald {

	private final JDA instance;

	public Archibald(String token) {
		JDABuilder builder = new JDABuilder(token);
		try {
			instance = builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create an Archibald. :(");
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new Archibald(new Scanner(System.in).nextLine());
	}
}
