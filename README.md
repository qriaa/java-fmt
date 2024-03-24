# Java formatter

## Setup
This assumes that you use IntelliJ Idea as specified in the course.
Make sure your IntelliJ ANTLR plugin supports the ANTLR `.jar` version you have.
1. Download ANTLR: [ANTLR downloads site](https://www.antlr.org/download.html) <br>
Make sure you download the complete version (Complete ANTLR <version> binaries jar).
2. Download picocli: [picocli 4.7.5 GitHub release](https://github.com/remkop/picocli/releases/tag/v4.7.5)
2. Right-click the `src` directory
   1. Open module settings
   2. Select dependencies tab
   3. Add the above downloaded `.jar`s
3. Generate the ANTLR classes with the use of the IntelliJ plugin
   1. Right-click the lexer file
   2. Configure ANTLR... 
   3. Make sure the following are selected:
      * generate parse tree listener
      * generate parse tree visitor
   4. Right-click the lexer file
   5. Generate ANTLR Recognizer
   6. Right-click the parser file
   7. Generate ANTLR Recognizer
4. Mark the `gen` directory as "Generated sources root"

You should be good to go.