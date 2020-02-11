package archibald.likes.packages.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.alixia.chatroom.api.QuickList;

import archibald.likes.packages.api.commands.BotCommandInvocation;
import archibald.likes.packages.api.commands.BotCommandNamespace;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.alixia.chatroom.api.QuickList;
import org.alixia.javalibrary.JavaTools;
import org.alixia.javalibrary.strings.matching.Matching;

public class SortingCommandNamespace extends BotCommandNamespace {
	{
		
		/*
		 * InsertionSort
		 * QuickSort
		 * BogoSort
		 * MergeSort
		 * 
		 * save and load list
		 * merge lists
		 * 
		 */
		//ArrayList<Object> saveList = new ArrayList<>();

		makeHelpCommand();

		addCommandHelp("sort",
				"Sorts a list of based off a given sorting algorithm.\nType `+sort:algorithms` for more information",
				"sort [args...]", "srt");
		new PublicCommand("sort", "srt") {
			@Override
			protected void run(BotCommandInvocation<MessageReceivedEvent> data) {
				if (data.args.length == 0) {
					reply(data, "You need to specify the items you want sorted");
				} else {
					String algo = data.args[0];
					String[] args = Arrays.copyOfRange(data.args, 1, data.args.length);
					long startTime = 0;
					long endTime = 0;
					long elapsedTime = 0;
					
					switch (algo) {
					case "-bbs":
						startTime = System.nanoTime();
						args = bubbleSort(args);
						endTime = System.nanoTime();
						break;
					case "-sls":
						startTime = System.nanoTime();
						args = selectionSort(args);
						endTime = System.nanoTime();
						break;
					default:
						replyAlgorithms(data);
						break;
					}
					elapsedTime = endTime - startTime;
					if (elapsedTime != 0)
						reply(data, "Result -> [" + String.join(", ", args) + "]" + "\n" + "Sort took " + elapsedTime
								+ " Nanoseconds");
				}
			}
		};
		
		addCommandHelp("SaveList")
		
		addCommandHelp("algorithms", "Displays the list of algorithms currently supported", "algorithms", "algo");
		new PublicCommand("algorithms", "algo") {

			@Override
			protected void run(BotCommandInvocation<MessageReceivedEvent> data) {
				replyAlgorithms(data);
			}
		};
		
		
	}

	// Algorithms
	public static <T extends Comparable<T>> T[] bubbleSort(T[] array) {
		int n = array.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (array[j].compareTo(array[j + 1]) > 0) {
					swap(array, j, j + 1);
				}
			}
		}
		return array;
	}

	
	public static <T extends Comparable<T>> T[] selectionSort(T[] array) {
		int minIndex;
		int n = array.length;
		for(int i = 0; i < n - 1; i++) {
			minIndex = i;
			for(int j = i + 1; j < n; j++) {
				if(array[minIndex].compareTo(array[j]) > 0) {
					minIndex = j;
				}
			}
			swap(array, minIndex, i);
		}
		return array;
	}
	
//	public static <T extends Comparable<T>, > T[] bogoSort(T[] array) 
//	{
//		final long STOPTIME = 10000;
//		long startTime = System.currentTimeMillis();
//		long endTime = 0;
//		while((endTime - startTime) <= STOPTIME) {
//			//if(){
//				return array;
//			}
//			endTime = System.currentTimeMillis();
//		}
//		
//	}
	
	
	public static <T extends Comparable<T>> T[] merge() {
		
	}
	
	public static void swap(Object[] array, int index1, int index2) {
		Object temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}
	
	public static void replyAlgorithms(BotCommandInvocation<MessageReceivedEvent> data) {
		String[] listOfAlgos = {
				"Name             ->\t[command]",
				"--------------------------------",
				"Bubble Sort      ->\t[-bbs]",
				"Selection Sort   ->\t[-sls]"
				};
		
		StringBuilder replyString = new StringBuilder("`+sort:sort [command] [args...]`\n");
		replyString.append("```");
		for (String s : listOfAlgos)
			replyString.append(s).append('\n');
		replyString.append("```");
		reply(data, replyString.toString());
	}
}
	

