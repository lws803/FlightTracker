import java.io.*;

import java.net.URL;
import java.net.MalformedURLException;
  
public class DownloadWebPage {
  
    public DownloadWebPage(String webpage,String fileName)
    {
        try {
            URL url = new URL(webpage);
            BufferedReader readr = 
              new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedWriter writer = 
              new BufferedWriter(new FileWriter("./resources/WebPages/"+fileName+".html"));
            String line;
            while ((line = readr.readLine()) != null) {
                writer.write(line);
            }
            readr.close();
            writer.close();
        }
        catch (MalformedURLException mue) {
            System.out.println("Exception raised");
        }
        catch (IOException ie) {
        }
        catch(Exception e) {}
    }
    

}
