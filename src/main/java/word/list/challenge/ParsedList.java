package word.list.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ParsedList {

  private final List<String> candidates;
  private final Set<String> fragments;

  private final int length;

  public ParsedList(
      List<String> candidates,
      Set<String> fragments,
      int length) {
    this.candidates = candidates;
    this.fragments = fragments;
    this.length = length;
  }

  public List<String> getCandidates() {
    return candidates;
  }

  public Stream<Composition> compositions() {
    return candidates.stream()
        .flatMap(this::splits)
        .filter(this::contains);
  }

  private Stream<Composition> splits(String s) {
    List<Composition> list = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
      list.add(new Composition(s.substring(0, i), s.substring(i)));
    }
    return list.stream();
  }

  private boolean contains(Composition composition) {
    return contains(composition.head()) && contains(composition.tail());
  }

  public boolean contains(String fragment) {
    return fragments.contains(fragment);
  }
}
