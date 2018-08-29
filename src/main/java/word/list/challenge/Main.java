package word.list.challenge;

import net.jbock.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
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
      Charset charset = inputCharset().map(Charset::forName).orElse(UTF_8);
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
      Charset charset = outputCharset().map(Charset::forName).orElse(UTF_8);
      String out = output().orElse("-");
      if ("-".equals(out)) {
        return new OutputStreamWriter(System.out, charset);
      }
      return new OutputStreamWriter(new FileOutputStream(Paths.get(out).toFile()), charset);
    }
  }

  public static void main(String[] args) {
    run(Main_Args_Parser.create().parseOrExit(args));
  }

  private static void run(Args args) {
    if (args.charsets()) {
      showCharsets();
      return;
    }
    CompositionFinder compositionFinder = new CompositionFinder(args.length().orElse(DEFAULT_LENGTH));
    try (BufferedReader in = new BufferedReader(args.in());
         PrintWriter out = new PrintWriter(new BufferedWriter(args.out()))) {
      compositionFinder.findCompositions(in).forEach(out::println);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void showCharsets() {
    Charset.availableCharsets().keySet().forEach(System.out::println);
  }
}
