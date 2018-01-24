package word.list.challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CompositionFinder {

  private final int length;

  CompositionFinder(int length) {
    this.length = length;
  }

  public Stream<Composition> findCompositions(BufferedReader source) throws IOException {
    List<String> candidates = new LinkedList<>();
    Set<String> fragments = new HashSet<>(30000);
    String line;
    while ((line = source.readLine()) != null) {
      int len = line.length();
      if (len == this.length) {
        candidates.add(line);
      } else if (len < this.length) {
        fragments.add(line);
      }
    }
    return candidates.stream().flatMap(this::splits)
        .filter(composition -> fragments.contains(composition.head()))
        .filter(composition -> fragments.contains(composition.tail()));
  }

  private Stream<Composition> splits(String s) {
    List<Composition> list = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
      list.add(new Composition(s.substring(0, i), s.substring(i)));
    }
    return list.stream();
  }
}
