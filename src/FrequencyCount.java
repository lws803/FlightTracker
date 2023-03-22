
import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.HashMap;

public class FrequencyCount {
	
	//main method
//	public static void main(String[] args) {
//		String[] strArr = htmlParse();
//		hashTable(strArr);
//	}

//creating separate method to parse from HTML
	
static String[] htmlParse (String file_Name){
	
	//fetching document
	String fileName = file_Name;

//	String fileName = "/Users/yashsodhi/Desktop/Java-Practice/W3C-Web-Pages/Data-W3C.htm";
	
    Document doc;
    
    //adding try block to check for exceptions
	try {
		doc = Jsoup.parse(new File(fileName), "utf-8");
	    String body = doc.body().text();
	    
	    //adding words in an array
	    String[] str =  body.split("\\s+");
	    for (int i = 0; i < str.length; i++) {
	        //deleting non alphanumeric characters
	        str[i] = str[i].replaceAll("[^\\w]", "");
	    }
	   
	    //System.out.println("Length of string is: "+ str.length);
	    
	    
	    return str;

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return null;
	
}

//inserting words in hash table

static void hashTable(String[] s){
	//initiating
	HashMap<String, Integer> frequency = new HashMap<String, Integer>();
	//counting word frequency
	for (int i = 0; i<s.length; i++) {
		if(s[i].equals("")) {
			continue;
		}
		if(frequency.get(s[i].toLowerCase()) != null) {
			
			int n = frequency.get(s[i].toLowerCase());
			frequency.put(s[i].toLowerCase(), n+1);
		} else {
			frequency.put(s[i].toLowerCase(), 1);
		}	
	}
	System.out.println("Words and their frequency are as follows");
	//printing key and value pair
	for (String hash: frequency.keySet()) {
		
	    String key = hash.toString();
	    String value = frequency.get(hash).toString();
	    System.out.println("["+key + " : " + value+"]");
	}
}
}
