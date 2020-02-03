package archibald.likes.packages.api.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.alixia.javalibrary.commands.GenericCommand;
import org.alixia.javalibrary.commands.GenericCommandConsumer;
import org.alixia.javalibrary.commands.GenericCommandManager;
import org.alixia.javalibrary.commands.OptionalGenericCommandConsumer;

public class CommandNamespace<D, N> extends GenericCommandManager<D> {
	private final Map<N, CommandNamespace<D, N>> children = new HashMap<>();

	public void addSubNamespace(N name, CommandNamespace<D, N> space) {
		children.put(name, space);
	}

	public final List<GenericCommand<? super D>> commandListView() {
		return commandView;
	}

	public final Stack<GenericCommandConsumer<? super D>> consumerListView() {
		return consumerView;
	}

	public Map<N, CommandNamespace<D, N>> getChildren() {
		return children;
	}

	public CommandNamespace<D, N> getSubNamespace(Iterable<N> namespaces) {
		CommandNamespace<D, N> cns = this;
		if (namespaces != null)
			for (final N n : namespaces)
				if (cns.getChildren().containsKey(n))
					cns = cns.getChildren().get(n);
				else
					return null;
		return cns;
	}

	public final Stack<OptionalGenericCommandConsumer<? super D>> optionalConsumerListView() {
		return optionalConsumerView;
	}

	/**
	 * <p>
	 * Runs the given command through this namespace, first checking if there are
	 * any specified namespaces (as denoted by the <code>namespaces</code>
	 * parameter) and delegating the command's running to a child appropriately, or
	 * simply running the command through this namespaces as a
	 * {@link GenericCommandManager} if not.
	 * </p>
	 * <p>
	 * If <code>namespaces</code> is <code>null</code> or {@link List#isEmpty()},
	 * this method automatically resorts to calling {@link #run(Object)}, with the
	 * given <code>cmd</code> as the parameter, and returning the value. Otherwise,
	 * the <b><font color=red>last</font></b> namespace in the specified
	 * {@link List} of namespaces is removed, and a child of this namespace,
	 * registered to this namespace with the namespace that was removed from the
	 * list, is searched for. If a child registered under the removed namespace is
	 * found, command invocation is delegated to it via calling
	 * {@link CommandNamespace#run(Object, List)} with the specified command and the
	 * remaining list of namespaces as arguments.
	 * </p>
	 *
	 * @param cmd        The command to run.
	 * @param namespaces The namespaces, in deepest-first, shallowest-last order.
	 *                   This parameter will be modified if non-<code>null</code>
	 *                   and not empty.
	 * @return <code>true</code> if the command was handled, <code>false</code>
	 *         otherwise.
	 */
	public boolean run(D cmd, List<N> namespaces) {
		if (namespaces == null || namespaces.isEmpty())
			return run(cmd);
		else {
			final N subspace = namespaces.remove(namespaces.size() - 1);
			if (children.containsKey(subspace))
				return children.get(subspace).run(cmd, namespaces);
			else
				return false;
		}
	}

}
