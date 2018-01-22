package word.list.challenge;

import java.util.List;
import java.util.Set;

public class ParsedList {

  private final List<String> candidates;
  private final Set<String> fragments;

  public ParsedList(
      List<String> candidates,
      Set<String> fragments) {
    this.candidates = candidates;
    this.fragments = fragments;
  }

  public List<String> getCandidates() {
    return candidates;
  }

  public boolean contains(String fragment) {
    return fragments.contains(fragment);
  }
}
