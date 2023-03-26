import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Std.StdOut;
import Trie.TrieST;

public class InvertedIndex {
	// Initialize a Trie data structure to store the inverted index
	static TrieST<Map<String, Integer>> wordTrie = new TrieST<>();

	static List<String> parseContent(String content) {
		// Split the content into individual words and store them in a list
		String[] str = content.split("\\s+");
		List<String> name = new ArrayList<String>();
		for (int i = 0; i < str.length; i++) {
			// Remove any non-alphanumeric characters and convert to lowercase
			name.add(str[i].replaceAll("[^\\w]", "").toLowerCase());
		}
		return name;
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

		// For each word, check if it is already in the trie. If not, add it with its
		// frequency
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
}
