import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;


public class PageRanking {
	public static String[] readFile(String filePath) {
		try {
			if (new File(filePath).isFile())
				return Jsoup.parse(new File(filePath), "utf-8").body().text().split("\\s+");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
		public Hashtable<String, Integer> matchPattern(String pattern) throws IOException{
			Hashtable<String, Integer> page_rank = new Hashtable<String, Integer>();
			String[] fileContent = null;
			File folder = new File("src/resc/Web Pages/");
			for (File fileEntry : folder.listFiles()) {
				fileContent = readFile(folder.getAbsolutePath() + "/" + fileEntry.getName());
				if (fileContent != null)
					for (int i=0;i<fileContent.length;i++) {
				Pattern p1 = Pattern.compile(pattern);
				// Now create matcher object.
				Matcher m = p1.matcher(fileContent[i]);
				while (m.find()) 
					page_rank.merge(fileEntry.getName(), 1, Integer::sum);
			}}
			return page_rank;
		}
}