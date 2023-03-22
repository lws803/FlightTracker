package WordCompletion;

import java.io.File;
import java.util.Scanner;

import Std.StdOut;
import Trie.TrieST;
import Common.Utils;

public class WordCompletion {
  private static Scanner sc;

  public static void main(String[] args) {

  }

  public static void run() throws Exception {
    String[] fileContent = null;
    TrieST<Integer> trie = new TrieST<Integer>();
    File folder = new File("src/resc/Web Pages/");
    for (File fileEntry : folder.listFiles()) {
      fileContent = Utils.readFile(folder.getAbsolutePath() + "/" + fileEntry.getName());
      if (fileContent != null)
        for (int i = 0; i < fileContent.length; i++)
          if (Utils.IsNumAlpha(fileContent[i]))
            trie.put(fileContent[i].toLowerCase(), i);
    }

    // Just for testing purposes
    Utils.writeFile("TrieDictionary", trie.keys().toString().replace(" ", "\n"));

    System.out.println("Enter a word:");
    String word = sc.nextLine();
    if (trie.contains(word)) {
      StdOut.println("");
    } else {
      StdOut.println("Related Words");
      for (String s : trie.keysWithPrefix(word))
        StdOut.println(s);
    }
  }

}
