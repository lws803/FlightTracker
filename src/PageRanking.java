import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Common.Utils;

public class PageRanking {

    /**
     * Matches a regular expression pattern against the content of each file in a
     * directory
     * and returns a Hashtable containing the file names as keys and their ranking
     * count as values.
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
            fileContent = Utils.readFile(folder.getAbsolutePath() + "/" + fileEntry.getName());
            if (fileContent != null) {
                // Iterate through each word in the file content and match it against the
                // pattern
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
