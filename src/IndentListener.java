import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;

import grammar.JavaParser;
import grammar.JavaParserBaseListener;

import java.util.List;

public class IndentListener extends JavaParserBaseListener {
    private final CommonTokenStream tokStream;
    TokenStreamRewriter rewriter;

    private int indentationLevel = 0;

    public IndentListener(CommonTokenStream tokens) {
        this.tokStream = tokens;
        rewriter = new TokenStreamRewriter(tokens);
    }

    @Override
    public void enterBlock(JavaParser.BlockContext ctx) {
        indentationLevel++;
    }

    @Override
    public void exitBlock(JavaParser.BlockContext ctx) {
        indentationLevel--;
    }

    @Override
    public void enterBlockStatement(JavaParser.BlockStatementContext ctx) {
        System.out.println("-----------");
        System.out.println(indentationLevel);
        System.out.println(ctx.start.getText());
        List<Token> spaces = tokStream.getHiddenTokensToLeft(ctx.start.getTokenIndex());
        if(spaces == null) {
            System.out.println("\"\"");
        } else {
            System.out.print("\"");
            for (Token whitespace : spaces) {
                System.out.print("ws:" + whitespace.getText());
            }
            System.out.println("\"");
        }
//        Token lastBefore = spaces.getLast();
        // try parsing up until first newline, if it doesn't work then recognize the token with the lexer
        // (move \n out of WS in the lexer into its own thing)
    }
}
