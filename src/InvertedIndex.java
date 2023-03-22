import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class InvertedIndex {
	 static TrieST<Map<String, Integer>> wordTrie = new TrieST<>();
	 static List<String> htmlParse(String url) {
	    //Document doc;
	    Document doc;
	    //adding try block to check for exceptions
		try {
			doc = Jsoup.connect(url).get();
		    String body = doc.body().text();
		    //adding words in an array
		    String[] str =  body.split("\\s+");
		    List<String> name = new ArrayList<String>();
		    for (int i = 0; i < str.length; i++) {
		        //deleting non alphanumeric characters
		        name.add(str[i].replaceAll("[^\\w]", "").toLowerCase());
		    }
		    //System.out.println("Length of string is: "+ str.length);
		    //System.out.println("Words are:" +name);
		    //System.out.println("The number of words are:" +name.size());
		    return name;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	 }
	static void constructTrie(String filename, List<String> words) {
		HashMap<String, Integer> wordFreq = new HashMap<String, Integer>();
		for(String word: words) {
			if(!wordFreq.containsKey(word)) {
				wordFreq.put(word,  1);
			} else {
				Integer x = wordFreq.get(word);
				wordFreq.replace(word, x+1);
			}
		}
		
		for(String key: wordFreq.keySet()) {
			if(!wordTrie.contains(key)) {
				HashMap<String, Integer> rowMap = new HashMap<String, Integer>();
				rowMap.put(filename, wordFreq.get(key));
				wordTrie.put(key, rowMap);
			}
		}
		
		for (String key : wordTrie.keys()) {
            StdOut.println(key + " " + wordTrie.get(key));
        }
		
	}
//	public static void main(String[] args) {
//		List<String> urlNames = new ArrayList<String>();
//		urlNames.add("https://skyscanner.ca/");
////		urlNames.add("https://www.ca.kayak.com/");
//		
//		for(String urls: urlNames) {
//			List<String> ret = htmlParse(urls);
//			constructTrie(urls, ret);
//		}
//		
//	}

}
