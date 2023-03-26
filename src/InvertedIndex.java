import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import Std.StdOut;
import Trie.TrieST;

public class InvertedIndex {
	// Initialize a Trie data structure to store the inverted index
	static TrieST<Map<String, Integer>> wordTrie = new TrieST<>();

	// Method to parse HTML content from a given URL and return a list of words
	static List<String> htmlParse(String url) {
		// Document doc;
		Document doc;
		// adding try block to check for exceptions
		try {
			// Connect to the URL and retrieve the HTML content
			doc = Jsoup.connect(url).get();
			String body = doc.body().text();
			// adding words in an array

			// Split the content into individual words and store them in a list
			String[] str = body.split("\\s+");
			List<String> name = new ArrayList<String>();
			for (int i = 0; i < str.length; i++) {
				// deleting non alphanumeric characters

				// Remove any non-alphanumeric characters and convert to lowercase
				name.add(str[i].replaceAll("[^\\w]", "").toLowerCase());
			}
			// System.out.println("Length of string is: "+ str.length);
			// System.out.println("Words are:" +name);
			// System.out.println("The number of words are:" +name.size());
			return name;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Print the stack trace if there is an exception
			e.printStackTrace();
		}
		return null;
	}

	// Method to construct the inverted index for a given file and list of words
	static void constructTrie(String filename, List<String> words) {
		// Create a hashmap to store the frequency of each word in the list
		HashMap<String, Integer> wordFreq = new HashMap<String, Integer>();
		for (String word : words) {
			if (!wordFreq.containsKey(word)) {
				wordFreq.put(word, 1);
			} else {
				// If the word is already in the hashmap, increment its frequency
				Integer x = wordFreq.get(word);
				wordFreq.replace(word, x + 1);
			}
		}

		// For each word, check if it is already in the trie. If not, add it with its frequency
		for (String key : wordFreq.keySet()) {
			if (!wordTrie.contains(key)) {
				HashMap<String, Integer> rowMap = new HashMap<String, Integer>();
				rowMap.put(filename, wordFreq.get(key));
				wordTrie.put(key, rowMap);
			}
		}

		// Print the inverted index
		for (String key : wordTrie.keys()) {
			StdOut.println(key + " " + wordTrie.get(key));
		}

	}
	// public static void main(String[] args) {
	// List<String> urlNames = new ArrayList<String>();
	// urlNames.add("https://skyscanner.ca/");
	//// urlNames.add("https://www.ca.kayak.com/");
	//
	// for(String urls: urlNames) {
	// List<String> ret = htmlParse(urls);
	// constructTrie(urls, ret);
	// }
	//
	// }

}
