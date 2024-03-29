import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

import grammar.*;

import static java.lang.Thread.sleep;

@Command(name = "java-fmt", mixinStandardHelpOptions = true)
public class JavaFmt implements Runnable {

    @Option(names={"-i", "--input-file"}, description="Input file path", required=true)
    String inputFile;

    @Option(names={"-c", "--config-file"}, description="Config file path\n" +
            "by default looks for java-fmt.json in the present working directory",
            defaultValue="java-fmt.json")
    String configFile;

    @Parameters(arity="1..*", description="Output file path")
    List<String> outputFiles;

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config;
        try {
            config = mapper.readTree(new File(configFile));
        } catch (JsonProcessingException e) {
            System.out.println("The config file is invalid JSON");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("The config file doesn't exist");
            throw new RuntimeException(e);
        }

        CharStream inp = null;

        try {
            inp = CharStreams.fromFileName(inputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JavaLexer lex = new JavaLexer(inp);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        JavaParser par = new JavaParser(tokens);

        ParseTree tree = par.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        IndentListener inserter = new IndentListener(lex, config.get("indentation"), tokens);
        walker.walk(inserter,tree);

//        Trees.inspect(tree, par);
//        try {
//            sleep(1000000000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println(inserter.rewriter.getText());
        try {
            var wr = new FileWriter(outputFiles.getFirst());
            wr.write(inserter.rewriter.getText());
            wr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        int exitCode = new CommandLine(new JavaFmt()).execute(args);
        System.exit(exitCode);
    }
}
