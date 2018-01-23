package word.list.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Parser {

  private final int length;

  public Parser(int length) {
    this.length = length;
  }

  public ParsedList parse(BufferedReader br) throws IOException {
    List<String> candidates = new LinkedList<>();
    Set<String> fragments = new HashSet<>(30000);
    String line;
    while ((line = br.readLine()) != null) {
      int len = line.length();
      if (len == this.length) {
        candidates.add(line);
      } else if (len < this.length) {
        fragments.add(line);
      }
    }
    return new ParsedList(candidates, fragments, length);
  }
}
