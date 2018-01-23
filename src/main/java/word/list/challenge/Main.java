package word.list.challenge;

import net.jbock.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {

  private static final int DEFAULT_LENGTH = 6;

  @CommandLineArguments(
      ignoreDashes = true,
      programName = "wordlist",
      missionStatement = "write dictionary words of given length as concatenations")
  abstract static class Args {

    @Positional
    @Description({"The source of the dictionary words.",
        "The source must be UTF-8 encoded and contain one word per line.",
        "A single dash '-' denotes standard in.",
        "Any other token will be interpreted as a file name."
    })
    abstract Optional<String> source();

    @Positional
    @Description({"Output stream for writing.",
        "A single dash '-' denotes standard out.",
        "Any other token will be interpreted as a file name.",
        "Defaults to standard out."})
    abstract Optional<String> output();

    @ShortName('n')
    @LongName("")
    @Description({"The length of the words that we're ",
        "trying to write as concatenations.",
        "Defaults to 6"})
    abstract OptionalInt length();

    @LongName("input-charset")
    @Description({"The charset of the input dictionary.",
        "Defaults to UTF-8."})
    abstract Optional<String> inputCharset();

    @LongName("output-charset")
    @Description({"The charset of the output stream.",
        "Defaults to UTF-8."})
    abstract Optional<String> outputCharset();

    @LongName("list-charsets")
    @Description("List available charsets.")
    abstract boolean charsets();

    Reader in() throws IOException {
      Charset charset = Charset.forName(inputCharset().orElse(UTF_8.toString()));
      if (isStdin()) {
        return new InputStreamReader(System.in, charset);
      }
      return new InputStreamReader(new FileInputStream(
          Paths.get(source().orElse("-")).toFile()), charset);
    }

    private boolean isStdin() {
      return source().orElse("-").equals("-");
    }

    Writer out() throws IOException {
      Charset charset = Charset.forName(outputCharset().orElse(UTF_8.toString()));
      String out = output().orElse("-");
      if ("-".equals(out)) {
        return new OutputStreamWriter(System.out, charset);
      }
      return new OutputStreamWriter(new FileOutputStream(Paths.get(out).toFile()), charset);
    }
  }

  public static void main(String[] args) {
    Main_Args_Parser.parse(args, System.err)
        .ifPresentOrElse(Main::run, () -> System.exit(1));
  }

  private static void run(Args args) {
    if (args.charsets()) {
      showCharsets();
      return;
    }
    Parser parser = new Parser(args.length().orElse(DEFAULT_LENGTH));
    try (BufferedReader in = new BufferedReader(args.in());
         PrintWriter out = new PrintWriter(new BufferedWriter(args.out()))) {
      ParsedList parsed = parser.parse(in);
      parsed.compositions().forEach(composition -> composition.print(out));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void showCharsets() {
    Map<String, Charset> cs = Charset.availableCharsets();
    for (Map.Entry<String, Charset> e : cs.entrySet()) {
      System.out.printf("%s%n", e.getKey());
    }
  }
}
