import picocli.CommandLine;
                import picocli.CommandLine.Command;
    import picocli.CommandLine.Option;
    import picocli.CommandLine.Parameters;

    import org.antlr.v4.gui.Trees;
	import org.antlr.v4.runtime.CharStream; //this line has a tab
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

import grammar.*;

import static java.lang.Thread.sleep;

@Command(name = "java-fmt", mixinStandardHelpOptions = true)
public class JavaFmt implements Runnable {
@Option(names={"-i", "--input-file"}, description="Input file path", required=true)
String inputFile;

@Parameters(arity="1..*", description="Output file path")
    List<String> outputFiles;

    @Override
public void run() {
    CharStream inp = null;

    if(true)
    System.out.println("true");

    while(false)
System.out.println("while false");

    if(false) {
System.out.println("hmm");
    }

        try {inp = CharStreams.fromFileName(inputFile);
    } catch (IOException e) {
        throw new RuntimeException(e);
        }
        JavaLexer lex = new JavaLexer(inp);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        JavaParser par = new JavaParser(tokens);

        ParseTree tree = par.compilationUnit(); /*weird inline comment*/ ParseTreeWalker walker = new ParseTreeWalker();
        IndentListener inserter = new IndentListener(tokens); walker.walk(inserter,tree);// should be broken into two lines

//        Trees.inspect(tree, par);
//        try {
//            sleep(1000000000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println(inserter.rewriter.getText());
        try {var wr = new FileWriter(outputFiles.getFirst()); // this should be in a new line
            wr.write(inserter.rewriter.getText());
            wr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        int exitCode = new CommandLine(new JavaFmt()).execute(args);System.exit(exitCode);
    }
}
