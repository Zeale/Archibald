package archibald.likes.packages.app;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class UserTracker {
	private final Map<User, String> msgMap = new HashMap<>();

	public void handleMessage(Message msg) {
		if (!msgMap.containsKey(msg.getAuthor()))
			msgMap.put(msg.getAuthor(), msg.getContentRaw());
	}

	public String getMsg(User user) {
		return msgMap.get(user);
	}

	private static UserTracker tracker;

	public static UserTracker getTracker() {
		return tracker == null ? (tracker = new UserTracker()) : tracker;
	}

	private UserTracker() {
	}

}
