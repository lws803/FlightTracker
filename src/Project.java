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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Project {
	private static Scanner sc;

	private static WebDriver webDriver;

	public static void main(String[] args) throws Exception {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		webDriver = new ChromeDriver(options);

		// Start data collection
		System.out.println("**************************");
		System.out.println("Welcome to Flight Analysis");
		System.out.println("***************************\n");
		Scanner in1 = null;
		Scanner in2 = null;

		try {
			in1 = new Scanner(System.in);
			in2 = new Scanner(System.in);

			File frequencyfile = new File("./src/search_frequency.txt");
			if (frequencyfile.exists()) {
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
				for (String key : wordfreq.keySet()) {
					System.out.println(key.toUpperCase() + " " + wordfreq.get(key) + " Times");
				}
				System.out.println("*************");
				System.out.println("*************");
			} else {
				frequencyfile.createNewFile();
			}

			Map<String, String> citiesCodes = new HashMap<String, String>();
			citiesCodes.put("windsor", "yqg");
			citiesCodes.put("toronto", "ytoa");
			citiesCodes.put("montreal", "yul");
			citiesCodes.put("vancouver", "yvr");

			System.out.println(
					"Please choose source and destination from the following: \nWindsor \nToronto \nMontreal \nVancouver ");
			System.out.println("-----------");
			System.out.println("Enter name of source");
			String src = in1.nextLine();

			while (!src.matches("[a-zA-Z_]+")) {
				System.out.println("Invalid source");
				System.out.println("Enter name of source");
				src = in2.nextLine();
			}

			src = src.toLowerCase();
			String fileword = src + "\n";
			String source = citiesCodes.get(src);
			if (source == null) {
				source = wordCheck(src);
				fileword = source + "\n";
				System.out.println(fileword);
				source = citiesCodes.get(source);
			}

			Files.write(Paths.get("src/search_frequency.txt"), fileword.getBytes(), StandardOpenOption.APPEND);

			System.out.println("-----------");
			System.out.println("Enter name of destination");

			String dest = in2.nextLine();
			while (!dest.matches("[a-zA-Z_]+")) {
				System.out.println("Invalid destination");
				System.out.println("Enter name of destination");
				dest = in1.nextLine();
			}

			dest = dest.toLowerCase();
			fileword = dest + "\n";
			String destination = citiesCodes.get(dest);

			if (destination == null) {
				destination = wordCheck(dest);
				fileword = destination + "\n";
				destination = citiesCodes.get(destination);
			}
			Files.write(Paths.get("src/search_frequency.txt"), fileword.getBytes(), StandardOpenOption.APPEND);

			System.out.println("-----------");
			System.out.println("Enter date of departure in the format : yymmdd");
			String departureDate = in1.nextLine();

			System.out.println("-----------");
			System.out.println("Enter number of people travelling");
			String count = in2.nextLine();

			String url = "https://www.skyscanner.ca/transport/flights/" + source + "/" + destination + "/" + departureDate
					+ "/?adultsv2=" + count
					+ "&cabinclass=economy&children=0&childrenv2=&destinationentityid=27537411&inboundaltsenabled=false&infants=0&originentityid=27536640&outboundaltsenabled=false&preferdirects=false&ref=home&rtn=0";

			List<String> urls = new ArrayList<String>();
			urls.add(url);
			crawlUrls(2, urls, new ArrayList<String>());

			// End data collection

			while (true) {
				boolean exit = false;
				System.out.println("\n*******************************************************************");
				System.out.println("Choose from the menu and enter the number corresponding to each menu");
				System.out.println("1 - Inverted Index");
				System.out.println("2 - Frequency Count");
				System.out.println("3 - Page Ranking");
				System.out.println("4 - Word Completion");
				System.out.println("5 - Exit");
				System.out.println("\nEnter your choice Number");
				int n;
				try {
					n = in2.nextInt();
					if (n > 0 && n < 6) {
						switch (n) {
							case 1:
								System.out.println("\n\n======================== Inverted Index ========================\n\n");

								for (File file : new File("src/resc/Web Pages/").listFiles()) {
									if (file.isFile() && file.getName().endsWith(".txt")) {
										String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
										if (content != null) {
											List<String> ret = InvertedIndex.parseContent(content);
											InvertedIndex.constructTrie(file.getName(), ret);
										}
									}
								}
								break;
							case 2:
								System.out.println("\n\n======================== Frequency Count ========================\n\n");

								for (File file : new File("src/resc/Web Pages/").listFiles()) {
									if (file.isFile() && file.getName().endsWith(".txt")) {
										String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
										if (content != null) {
											System.out.println("\n"+file.getName());
											String[] strArr = FrequencyCount
													.parseContent(content);
											FrequencyCount.printWordFrequency(strArr);
										}
									}
								}
								break;
							case 3:
								System.out.println("\n\n======================== Page Ranking ========================\n\n");
								System.out.print("Enter word: ");
								sc = new Scanner(System.in);
								String input = sc.nextLine();
								PageRanking pg = new PageRanking();

								Hashtable<String, Integer> pageRank = pg.matchPattern(input);

								if (pageRank.size() == 0)
									System.out.println("Not found");
								else {
									int totalOccurences = 0;
									for (int occurences : pageRank.values())
										totalOccurences += occurences;
									System.out.println("About " + totalOccurences + " matches ");
									System.out.println("Matches found in " + pageRank.size() + " web pages.\n");
									System.out.println("Matches\t Top 10 Pages");
									Map<String, Integer> sortedByValueDesc = pageRank.entrySet()
											.stream().limit(10)
											.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
											.collect(
													Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
									sortedByValueDesc.forEach((key, value) -> System.out.println("  " + value + " -- " + key));
								}
								break;
							case 4:
								System.out.println("\n\n======================== Word Completion ========================\n\n");
								WordCompletion.run();
								break;
							default:
								exit = true;
						}
						if (exit)
							break;

					} else
						System.out.println("Oops! Entered Wrong number");

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			in1.close();
			in2.close();
		}
	}

	private static void crawlUrls(int level, List<String> urls, ArrayList<String> visited) throws Exception {

		// TODO Auto-generated method stub
		for (String url : urls) {
			crawl(level, url, visited);
		}
		webDriver.quit();
	}

	private static void crawl(int level, String targetLink, ArrayList<String> visited)
			throws Exception {

		System.out.println(targetLink);
		if (level <= 3) {
			Document doc = request(targetLink, visited);

			if (doc != null && doc.title() != "") {
				// We write the doc to folder if it is non-null
				System.out.println("Scanning... " + doc.title());
				BufferedWriter writer = new BufferedWriter(
						new FileWriter("./src/resc/Web Pages/" + doc.title() + ".txt"));
				writer.write(doc.text().toLowerCase());
				writer.close();
			}

			if (doc != null) {
				for (Element link : doc.select("a[href]")) {
					String nextLink = link.attr("href");

					String regex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
					Pattern pattern = Pattern.compile(regex);

					if (pattern.matcher(nextLink).matches() && visited.contains(nextLink) == false) {
						crawl(level++, nextLink, visited);
					}
				}
			}
		}
	}

	private static Document request(String targetLink, ArrayList<String> v) throws Exception {
		try {
			webDriver.get(targetLink);
		} catch (Exception exception) {
			return null;
		}

		String[] ss = null;
		List<Integer> priceList = new ArrayList<Integer>();

		int attempts = 0;
		while (attempts < 2) {
			List<WebElement> elements1 = webDriver.findElements(By.className("FlightsTicket_container__NWJkY"));
			List<String> flightDetails = new ArrayList<String>();

			if (elements1.size() > 0) {
				for (WebElement element : elements1) {
					ss = element.getText().split("\n");
					String str = element.getText();
					flightDetails.add(str);
					for (int i = 0; i < ss.length; i++) {
						// Find price
						if (ss[i].startsWith("C$")) {
							priceList.add(Integer.parseInt(ss[i].replaceAll("[^0-9]", "")));
						}
					}
				}

				System.out.println("PriceList:" + priceList);
				int lowestPrice = priceList.indexOf(Collections.min(priceList));
				System.out.println("Lowest Price Flight:" + flightDetails.get(lowestPrice));
				break;
			}
			attempts++;
		}

		WebElement element = webDriver.findElement(By.tagName("body"));
		// Get the HTML content of the web element
		String htmlContent = element.getAttribute("innerHTML");
		// Convert the HTML content to a JSoup document
		Document document = Jsoup
				.parse("<html><head><title>" + webDriver.getTitle() + "</title></head><body>" + htmlContent + "</body></html>");

		return document;
	}

	public static int minDistance(String firstWrd, String secondWrd) {

		// assigning length of both words to variables
		int x = firstWrd.length();
		int y = secondWrd.length();

		// assigning both lengths to an array
		int[][] editDist = new int[x + 1][y + 1];

		// Dynamic Programming algorithm
		for (int i = 0; i <= x; i++)
			editDist[i][0] = i;
		for (int i = 1; i <= y; i++)
			editDist[0][i] = i;

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				if (firstWrd.charAt(i) == secondWrd.charAt(j))
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

	// Function to parse HTML file and store in an Array
	static Set<String> htmlParse() {
		// fetching document
		String url = "http://www.citymayors.com/gratis/canadian_cities.html";
		Document doc;
		// adding try block to check for exceptions
		try {
			doc = Jsoup.connect(url).get();
			String body = doc.body().text();
			// adding words in an array
			String[] str = body.split("\\s+");
			Set<String> name = new HashSet<>();
			for (int i = 0; i < str.length; i++) {
				// deleting non alphanumeric characters
				name.add(str[i].replaceAll("[^\\w]", "").toLowerCase());
			}
			// System.out.println("Length of string is: "+ str.length);
			return name;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This finds the closest word in case there is a typo just for cities
	 * by checking a list of cities from a fixed URL.
	 */
	public static String wordCheck(String indexWord) {

		// using array list to store the resulting list
		ArrayList<String> resultsArray = new ArrayList<String>();

		// converting to lower-case to have uniform results
		indexWord = indexWord.toLowerCase();
		int minDistance = Integer.MAX_VALUE;

		// Parsed Array stored in Set
		Set<String> wordsArray = htmlParse();

		for (String word : wordsArray) {
			int distance = minDistance(indexWord, word);
			if (distance <= minDistance) {
				minDistance = distance;
			}
			// condition for being similar words is having edit distance of 2
			if (distance <= 2) {
				resultsArray.add(word);
			}
		}

		// Printing list of similar words
		System.out.println("Did you mean " + indexWord + ":");
		System.out.println(resultsArray);
		String word = resultsArray.get(0);
		return word;
	}

}
