package word.list.challenge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.OptionalInt;

import net.jbock.*;

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
    abstract String source();

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

    BufferedReader in() throws IOException {
      if ("-".equals(source())) {
        return new BufferedReader(new InputStreamReader(System.in));
      }
      return new BufferedReader(new FileReader(Paths.get(source()).toFile()));
    }

    BufferedWriter out() throws IOException {
      String out = output().orElse("-");
      if ("-".equals(out)) {
        return new BufferedWriter(new OutputStreamWriter(System.out));
      }
      return new BufferedWriter(new FileWriter(Paths.get(out).toFile()));
    }

    int len() {
      return length().orElse(DEFAULT_LENGTH);
    }
  }

  public static void main(String[] args) {
    Main_Args_Parser.parse(args, System.err)
        .ifPresentOrElse(Main::run, () -> System.exit(1));
  }

  private static void run(Args args) {
    Parser parser = new Parser(args.len());
    try (BufferedReader in = args.in();
         PrintWriter out = new PrintWriter(args.out())) {
      ParsedList parsed = parser.parse(in);
      for (String candidate : parsed.getCandidates()) {
        for (int i = 0; i < args.len() - 1; i++) {
          String head = candidate.substring(0, i);
          if (parsed.contains(head)) {
            String tail = candidate.substring(i);
            if (parsed.contains(tail)) {
              out.format("%s + %s => %s\n", head, tail, candidate);
            }
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
