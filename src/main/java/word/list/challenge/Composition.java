package word.list.challenge;

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

  public String toString() {
    return String.format("%s + %s => %s%s", head, tail, head, tail);
  }
}
