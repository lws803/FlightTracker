import java.util.HashMap;

public class FrequencyCount {

	static String[] parseContent(String content) {

		int x;
		// inserting strings/words in the array based on whitespace
		String[] word = content.split("\\s+");
		for (x = 0; x < word.length; x++) {
			// removing any non-alphanumeric characters
			word[x] = word[x].replaceAll("[^\\w]", "");
		}

		return word;
	}

	// defining a method for counting the frequency of words using HashMap

	static void printWordFrequency(String[] wordList) {
		// Initializing HashMap for frequency
		HashMap<String, Integer> frequencyObj = new HashMap<String, Integer>();
		int x;
		// counting frequency of the word
		for (x = 0; x < wordList.length; x++) {
			if (wordList[x].equals("")) {
				continue;
			}
			if (frequencyObj.get(wordList[x].toLowerCase()) != null) {

				int count = frequencyObj.get(wordList[x].toLowerCase());
				frequencyObj.put(wordList[x].toLowerCase(), count + 1);
			} else {
				frequencyObj.put(wordList[x].toLowerCase(), 1);
			}
		}
		System.out.println("Words with their corresponding frequency are given below:");
		// printing key and value pair as word with their frequency
		for (String hash : frequencyObj.keySet()) {

			String word = hash.toString();
			String frequency = frequencyObj.get(hash).toString();
			System.out.println("[" + word + " : " + frequency + "]");
		}
	}
}
