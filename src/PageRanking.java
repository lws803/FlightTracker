import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;

public class PageRanking {

    /**
     * Reads the contents of a file and returns an array of strings containing
     * the file content split by whitespace characters.
     *
     * @param filePath the path of the file to read
     * @return an array of strings containing the file content, or null if the file doesn't exist
     * @throws IOException if there is an error reading the file
     */
    public static String[] readFile(String filePath) throws IOException {
        // Check if the file exists
        if (new File(filePath).isFile()) {
            // Parse the file and split its content by whitespace characters
            return Jsoup.parse(new File(filePath), "utf-8").body().text().split("\\s+");
        } else {
            return null;
        }
    }

    /**
     * Matches a regular expression pattern against the content of each file in a directory
     * and returns a Hashtable containing the file names as keys and their ranking count as values.
     *
     * @param pattern the regular expression pattern to match
     * @return a Hashtable containing the file names and their ranking count
     * @throws IOException if there is an error reading the files
     */
    public Hashtable<String, Integer> matchPattern(String pattern) throws IOException {
        Hashtable<String, Integer> pageRank = new Hashtable<String, Integer>();
        String[] fileContent;
        File folder = new File("src/resc/Web Pages/");
        // Iterate through each file in the folder
        for (File fileEntry : folder.listFiles()) {
            fileContent = readFile(folder.getAbsolutePath() + "/" + fileEntry.getName());
            if (fileContent != null) {
                // Iterate through each word in the file content and match it against the pattern
                for (int i = 0; i < fileContent.length; i++) {
                    Pattern p1 = Pattern.compile(pattern);
                    // Now create matcher object
                    Matcher m = p1.matcher(fileContent[i]);
                    while (m.find()) {
                        // Add 1 to the file's ranking count in the Hashtable
                        pageRank.merge(fileEntry.getName(), 1, Integer::sum);
                    }
                }
            }
        }
        return pageRank;
    }
}
