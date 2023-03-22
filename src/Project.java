import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
public class Project {
	private static Scanner myObj;
	private static Scanner sc;
	public static void main(String[] args) throws Exception {
		File frequencyfile = new File("./src/search_frequency.txt");
		if(frequencyfile.exists()) {
		List<String> words = new ArrayList<String>();
	    Scanner myReader = new Scanner(frequencyfile);
	    while (myReader.hasNextLine()) {
	       words.add(myReader.nextLine());
	    }
	    myReader.close();
	    Map<String, Integer> wordfreq = new HashMap<>();
	    for (String word : words) {
	    	Integer integer = wordfreq.get(word);
	    	if (integer == null)
	    		wordfreq.put(word, 1);
	    	else {
	    		wordfreq.put(word, integer + 1);
	    	}
	    }
	    System.out.println("*************");
	    System.out.println("*************");
	    System.out.println("MOST SEARCHED DESTINATIONS THIS YEAR:");
	    for (String key: wordfreq.keySet()){
            System.out.println(key.toUpperCase() + " " + wordfreq.get(key) + " Times");
        }
	    System.out.println("*************");
	    System.out.println("*************");}
		else {
			frequencyfile.createNewFile();
		}
		myObj = new Scanner(System.in);
		Map<String, String> citiesCodes = new HashMap<String, String>();
		citiesCodes.put("windsor", "yqg");
		citiesCodes.put("toronto", "ytoa");
		citiesCodes.put("montreal", "yul");
		citiesCodes.put("vancouver", "yvr");
		System.out.println("Please choose source and destination from the following: \nWindsor \nToronto \nMontreal \nVancouver ");
		System.out.println("-----------");
	    System.out.println("Enter name of source");
	    String src = myObj.nextLine();
	    
	    while(!src.matches("[a-zA-Z_]+")){
	        System.out.println("Invalid source");
	        System.out.println("Enter name of source");
	        src = myObj.nextLine();
	    }
	    src = src.toLowerCase();
	    String fileword = src + "\n";
	    String source = citiesCodes.get(src);
	    if(source==null) {
	    	source=wordCheck(src);
	    	fileword = source + "\n";
	    	System.out.println(fileword);
	    	source = citiesCodes.get(source);
	    }
	    
	    Files.write(Paths.get("src/search_frequency.txt"), fileword.getBytes(), StandardOpenOption.APPEND);
	    
	    System.out.println("-----------");
	    System.out.println("Enter name of destination");

	    String dest = myObj.nextLine();
	    while(!dest.matches("[a-zA-Z_]+")){
	        System.out.println("Invalid destination");
	        System.out.println("Enter name of destination");
	        dest = myObj.nextLine();
	    }
	    dest = dest.toLowerCase();
	    fileword = dest + "\n";
	    String destination = citiesCodes.get(dest);
	    if(destination==null) {
	    	destination=wordCheck(dest);
	    	fileword = destination + "\n";
	    	destination = citiesCodes.get(destination);
	    }
	    Files.write(Paths.get("src/search_frequency.txt"), fileword.getBytes(), StandardOpenOption.APPEND);
	    
	    System.out.println("-----------");
	    System.out.println("Enter date of departure in the format : yymmdd");
	    String departureDate = myObj.nextLine();
	    
	    System.out.println("-----------");
	    System.out.println("Enter number of people travelling");
	    String count = myObj.nextLine();
	    
	    System.out.println("-----------");
	    System.out.println("Enter class : economy or premium economy");
	    String seatType = myObj.nextLine();
	    
	    String url = "https://www.skyscanner.ca/transport/flights/"+source+"/"+destination+"/"+departureDate+"/?adults="+count+"&adultsv2=1&cabinclass=economy&children=0&childrenv2=&destinationentityid=27537411&inboundaltsenabled=false&infants=0&originentityid=27536640&outboundaltsenabled=false&preferdirects=false&ref=home&rtn=0";
		
		List<String> urls = new ArrayList<String>();
		urls.add(url);
		crawlUrls(2, urls, new ArrayList<String>());
		Document document = Jsoup.connect(url).get();
        document.text();
        
        String[] title = document.title().split("\\|");
        String newTitle = "";
		for(String s : title) {
			newTitle = newTitle+""+s;
		}
        BufferedWriter writer = 
                new BufferedWriter(new FileWriter("./src/resc/Web Pages/"+newTitle+".txt"));
        writer.write(document.text().toLowerCase());
        writer.close();	
        System.out.println("\n\n======================== Features ======================== \n\n"
        		+ "======================== Inverted Index ========================\n\n");
        List<String> urlNames = new ArrayList<String>();
		urlNames.add(url);
		
		for(String links: urlNames) {
			List<String> ret = InvertedIndex.htmlParse(links);
			InvertedIndex.constructTrie(links, ret);
		}
		System.out.println("\n\n======================== Frequency Count ========================\n\n");
		String[] strArr = FrequencyCount.htmlParse("./src/resc/Web Pages/Cheap domestic flights from Toronto.txt");
		FrequencyCount.hashTable(strArr);
		
		System.out.println("\n\n======================== Page Ranking ========================\n\n");
		System.out.print("Enter word: ");
		sc = new Scanner(System.in);
		String input = sc.nextLine();
		PageRanking pg= new PageRanking();
		
		Hashtable<String, Integer> page_rank = pg.matchPattern(input);
		
		if(page_rank.size()==0)	
			System.out.println("Not found");
		else {
			int totalOccurences=0;
			for(int occurences : page_rank.values())
				totalOccurences += occurences;
			System.out.println("About "+totalOccurences+" matches ");
			System.out.println("Matches found in "+page_rank.size()+" web pages.\n");
			System.out.println("Matches\t Top 10 Pages");
			Map<String, Integer> sortedByValueDesc = page_rank.entrySet() 
					.stream().limit(10)
					.sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) 
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			sortedByValueDesc.forEach((key, value) -> System.out.println("  "+value + " -- " + key));  
		}

		System.out.println("\n\n======================== Word Completion ========================\n\n");
		String[] fileContent=null;
		TrieST<Integer> t = new TrieST<Integer>();
		File folder = new File("src/resc/Web Pages/");
		for (File fileEntry : folder.listFiles()) {
			fileContent = readFile(folder.getAbsolutePath() + "/" + fileEntry.getName());
			if (fileContent != null)
				for (int i=0;i<fileContent.length;i++)
					if (IsNumAlpha(fileContent[i]))
						t.put(fileContent[i].toLowerCase(), i);
		}
		writeFile("TrieDictionary",t.keys().toString().replace(" ","\n"));

		

		System.out.println("Enter a word:");
		String word = sc.nextLine();
		if(t.contains(word)) {
			StdOut.println("");
		}
		else{
			StdOut.println("Related Words");
		    for (String s : t.keysWithPrefix(word))
		        StdOut.println(s);
		}

		
		
}
	
	
	
	
	private static void crawlUrls(int level, List<String> urls, ArrayList<String> visited) throws Exception {
		// TODO Auto-generated method stub
		for(String url : urls) {
			crawl(level, url, visited);
		}
		
	}

	private static void crawl (int level, String next_link2, ArrayList<String> visited) throws Exception {
		
		System.out.println(next_link2);
		if (level <= 3) {
			Document doc = request(next_link2,visited);
			
			if (doc != null) {
				for (Element link : doc.select("a[href]")) {
					String next_link = link.absUrl("href");
					if (visited.contains(next_link) == false) {
						crawl(level++, next_link, visited);
					}
				}
			}
		}
			
	}
	
	private static Document request(String next_link2, ArrayList<String> v) throws Exception {
	
		WebDriver webDriver = new ChromeDriver();
		webDriver.get(next_link2);
		String[] ss = null;
		List<Integer> priceList = new ArrayList<Integer>();
		List<WebElement> elements1 =  webDriver.findElements(By.className("FlightsTicket_container__NWJkY"));
		List<String> flightDetails = new ArrayList<String>();
		for(WebElement element : elements1) {
			ss = element.getText().split("\n");
			String str = element.getText();
			flightDetails.add(str);
			for (int i=0;i<ss.length; i++) {
				if(ss[i].startsWith("C$")) {
					priceList.add(Integer.parseInt(ss[i].replaceAll("[^0-9]", "")));
				}
			}
		}
		System.out.println("PriceList:"+ priceList);
		int lowestPrice = priceList.indexOf(Collections.min(priceList));
		System.out.println("Lowest Price Flight:"+ flightDetails.get(lowestPrice));
		return null;
		
	}
	
	public static int minDistance(String firstWrd, String secondWrd) {
		
		//assigning length of both words to variables
        int x = firstWrd.length();
        int y = secondWrd.length();
        
        //assigning both lengths to an array
        int[][] editDist = new int[x + 1][y + 1];
        
        //Dynamic Programming algorithm
        for(int i = 0; i <= x; i++)
        	editDist[i][0] = i;
        for(int i = 1; i <= y; i++)
        	editDist[0][i] = i;
        
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                if(firstWrd.charAt(i) == secondWrd.charAt(j))
                	editDist[i + 1][j + 1] = editDist[i][j];
                else {
                    int a = editDist[i][j];
                    int b = editDist[i][j + 1];
                    int c = editDist[i + 1][j];
                    editDist[i + 1][j + 1] = a < b ? (a < c ? a : c) : (b < c ? b : c);
                    editDist[i + 1][j + 1]++;
                }
            }
        }
        return editDist[x][y];
    }
	
	//Function to parse HTML file and store in an Array
	static Set<String> htmlParse (){
		//fetching document
		String url = "http://www.citymayors.com/gratis/canadian_cities.html";
	    Document doc;
	    //adding try block to check for exceptions
		try {
			doc = Jsoup.connect(url).get();
		    String body = doc.body().text();
		    //adding words in an array
		    String[] str =  body.split("\\s+");
		    Set<String> name = new HashSet<>();
		    for (int i = 0; i < str.length; i++) {
		        //deleting non alphanumeric characters
		        name.add(str[i].replaceAll("[^\\w]", "").toLowerCase());
		    }
		    //System.out.println("Length of string is: "+ str.length);
		   return name;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String wordCheck(String indexWord) {
		
		//using array list to store the resulting list
		ArrayList<String> resultsArray = new ArrayList<String>();
		
		//converting to lower-case to have uniform results
		indexWord = indexWord.toLowerCase();
		int minDistance = Integer.MAX_VALUE;
		
		//Parsed Array stored in Set
		Set<String> wordsArray = htmlParse();
		
		for(String word : wordsArray) {
			int distance = minDistance(indexWord, word);
			if(distance <= minDistance) {
				minDistance = distance;
			}
			//condition for being similar words is having edit distance of 2
			if(distance <=2) {
				resultsArray.add(word);
			}
		}
		
		//Printing list of similar words
		System.out.println("Did you mean " + indexWord + ":");
		System.out.println(resultsArray);
		String word = resultsArray.get(0);
		return word;
	}
	public static String[] readFile(String filePath) {
		try {
			if (new File(filePath).isFile())
				return Jsoup.parse(new File(filePath), "utf-8").body().text().split("\\s+");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	public static boolean IsNumAlpha(String str) {
		return str != null && str.matches("^[a-zA-Z0-9]*$");
	}
	public static void writeFile(String name, String content) throws Exception {
		File f = new File("./src/" + name + ".txt");
		if (f.exists())
			f.delete();
		f.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(content);
		bw.close();
	}
}

