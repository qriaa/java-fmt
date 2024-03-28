import grammar.JavaLexer;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;

import grammar.JavaParser;
import grammar.JavaParserBaseListener;

import java.util.List;

public class IndentListener extends JavaParserBaseListener {
    private final CommonTokenStream tokStream;
    private final JavaLexer lexer;
    TokenStreamRewriter rewriter;

    private int indentationLevel = 0;
    public String indentationString = "    ";

    public IndentListener(JavaLexer lexer, CommonTokenStream tokens) {
        this.tokStream = tokens;
        this.lexer = lexer;
        rewriter = new TokenStreamRewriter(tokens);
    }

    private String getTokenType(Token token) {
        return lexer.getVocabulary().getDisplayName(token.getType());
    }

    private void indentLine(Token startToken) {
        System.out.println("-----------");
        System.out.println(indentationLevel);
        System.out.println(startToken.getText());
        List<Token> spaces = tokStream.getHiddenTokensToLeft(startToken.getTokenIndex());
        //System.out.println("\"" + spaces.getLast().getText() + "\"");
        boolean prependNewline = false;

        if(spaces == null) {
            prependNewline = true;
        } else {
            while (!spaces.isEmpty() && getTokenType(spaces.getLast()).equals("WS")) {
                rewriter.delete(spaces.getLast());
                spaces.removeLast();
            }
            if(spaces.isEmpty() || !getTokenType(spaces.getLast()).equals("NEWLINE")) {
                prependNewline = true;
            }
        }

        rewriter.insertBefore(startToken, indentationString.repeat(indentationLevel));
        if(prependNewline)
            rewriter.insertBefore(startToken, "\n");

//        System.out.print("\"");
//        for (Token whitespace : spaces) {
//            System.out.print(lexer.getVocabulary().getDisplayName(whitespace.getType()) + ":" + whitespace.getText());
//        }
//        System.out.println("\"");
    }

    @Override
    public void enterBlock(JavaParser.BlockContext ctx) {
        System.out.println("block +1");
        indentationLevel++;
    }

    @Override
    public void exitBlock(JavaParser.BlockContext ctx) {
        indentationLevel--;
    }

    @Override
    public void enterClassBody(JavaParser.ClassBodyContext ctx) {
        indentationLevel++;
    }

    @Override
    public void exitClassBody(JavaParser.ClassBodyContext ctx) {
        indentationLevel--;
    }
//
//    @Override
//    public void enterInterfaceBody(JavaParser.InterfaceBodyContext ctx) {
//        indentationLevel++;
//    }
//
//    @Override
//    public void exitInterfaceBody(JavaParser.InterfaceBodyContext ctx) {
//        indentationLevel--;
//    }

    @Override
    public void enterBlockStatement(JavaParser.BlockStatementContext ctx) {
        indentLine(ctx.start);
    }

    @Override
    public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
        indentLine(ctx.start);
    }

    @Override
    public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
        indentLine(ctx.start);
    }

    @Override
    public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
        if(ctx.memberDeclaration() != null) {
            List<JavaParser.ModifierContext> modifiers =  ctx.modifier();
            int lastAnnotationIndex = -1;

            for(int i = 0; i < modifiers.size(); i++) {
                JavaParser.ClassOrInterfaceModifierContext COIModifier =
                        modifiers.get(i).classOrInterfaceModifier();
                if(COIModifier == null)
                    continue;
                if(COIModifier.annotation() == null)
                    continue;
                lastAnnotationIndex = i;
                indentLine(COIModifier.annotation().start);
            }

            if(lastAnnotationIndex == -1)
                return;
            else if(lastAnnotationIndex+1 >= modifiers.size())
                indentLine(ctx.memberDeclaration().start);
            else
                indentLine(modifiers.get(lastAnnotationIndex+1).start);
        } else {
            indentLine(ctx.start);
        }
    }

    @Override
    public void enterInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx) {
        indentLine(ctx.start);
    }

//    @Override
//    public void enterAnnotation(JavaParser.AnnotationContext ctx) {
//        indentLine(ctx.start);
//    }
}
