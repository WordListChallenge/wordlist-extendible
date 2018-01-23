package word.list.challenge;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Composition {

  private final String head;
  private final String tail;

  public Composition(String head, String tail) {
    this.head = head;
    this.tail = tail;
  }

  public String head() {
    return head;
  }

  public String tail() {
    return tail;
  }

  public void print(PrintWriter out) {
    out.format("%s + %s => %s%s\n", head, tail, head, tail);
  }

  public String toString() {
    StringWriter writer = new StringWriter();
    print(new PrintWriter(writer));
    return writer.toString();
  }
}
