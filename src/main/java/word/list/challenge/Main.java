package word.list.challenge;

import net.jbock.CommandLineArguments;
import net.jbock.Parameter;
import net.jbock.PositionalParameter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.OptionalInt;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {

  private static final int DEFAULT_LENGTH = 6;

  @CommandLineArguments(
      allowPrefixedTokens = true,
      programName = "wordlist",
      missionStatement = "write dictionary words of given length as concatenations")
  abstract static class Args {

    /**
     * The source of the dictionary words.
     * The source must contain one word per line.
     * A single dash '-' denotes standard in.
     * Any other token will be interpreted as a file name.
     */
    @PositionalParameter(optional = true)
    abstract Optional<String> source();

    /**
     * Output stream for writing.
     * A single dash '-' denotes standard out.
     * Any other token will be interpreted as a file name.
     * Defaults to standard out.
     */
    @PositionalParameter(optional = true, position = 1)
    abstract Optional<String> output();

    /**
     * The length of the words that we're
     * trying to write as concatenations.
     * Defaults to 6
     */
    @Parameter(optional = true, shortName = 'n', longName = "")
    abstract OptionalInt length();

    /**
     * The charset of the input dictionary.
     * Defaults to UTF-8.
     */
    @Parameter(optional = true, longName = "input-charset")
    abstract Optional<Charset> inputCharset();

    /**
     * The charset of the output stream.
     * Defaults to UTF-8.
     */
    @Parameter(optional = true, longName = "output-charset")
    abstract Optional<Charset> outputCharset();

    /**
     * List available charsets.
     */
    @Parameter(flag = true, longName = "list-charsets")
    abstract boolean charsets();

    Reader in() throws IOException {
      Charset charset = inputCharset().orElse(UTF_8);
      if (isStdin()) {
        return new InputStreamReader(System.in, charset);
      }
      return new InputStreamReader(new FileInputStream(
          source().map(Paths::get).map(Path::toFile).orElseThrow(AssertionError::new)),
          charset);
    }

    private boolean isStdin() {
      return source().orElse("-").equals("-");
    }

    Writer out() throws IOException {
      Charset charset = outputCharset().orElse(UTF_8);
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
