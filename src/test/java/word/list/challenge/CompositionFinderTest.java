package word.list.challenge;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class CompositionFinderTest {

  @Test
  void parse() throws IOException {
    BufferedReader in = new BufferedReader(new StringReader(
        Stream.of("123456", "123", "456").collect(joining("\n"))));
    CompositionFinder compositionFinder = new CompositionFinder(6);
    List<Composition> compositions = compositionFinder.findCompositions(in)
        .collect(toList());
    assertEquals(1, compositions.size());
    assertEquals("123", compositions.get(0).head());
    assertEquals("456", compositions.get(0).tail());
  }

  @Test
  void compositions() throws IOException {
    BufferedReader in = new BufferedReader(new StringReader(
        Stream.of("123456", "12", "3456", "1234", "56").collect(joining("\n"))));
    CompositionFinder compositionFinder = new CompositionFinder(6);
    List<Composition> compositions = compositionFinder.findCompositions(in)
        .sorted(comparing(Composition::head))
        .collect(toList());
    assertEquals(2, compositions.size());
    assertEquals("12", compositions.get(0).head());
    assertEquals("3456", compositions.get(0).tail());
    assertEquals("1234", compositions.get(1).head());
    assertEquals("56", compositions.get(1).tail());
  }
}
