package WordCompletion;

import java.io.File;
import java.util.Scanner;

import Std.StdOut;
import Trie.TrieST;
import Common.Utils;

public class WordCompletion {
  private static Scanner scanner;

  public static void run() throws Exception {
    scanner = new Scanner(System.in);

    String[] fileContent = null;
    TrieST<Integer> trie = new TrieST<Integer>();
    File folder = new File("src/resc/Web Pages/");

    // Reads the contents of files in the "src/resc/Web Pages/" directory (one at a
    // time)
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
    String word = scanner.nextLine();

    if (trie.contains(word)) {
      // If the word exists in the trie, prints a blank line to the console.
      StdOut.println("");
    } else {
      // If the word does not exist in the trie, prints "Related Words" to the console
      StdOut.println("Related Words");
      for (String s : trie.keysWithPrefix(word))
        StdOut.println(s);
    }
  }

}
