# Java formatter

## Setup
This assumes that you use IntelliJ Idea as specified in the course.
Make sure your IntelliJ ANTLR plugin supports the ANTLR `.jar` version you have.
1. Use Maven to download the dependencies
2. Generate the ANTLR classes with the use of the IntelliJ plugin
   1. Right-click the lexer file
   2. Configure ANTLR... 
   3. Make sure the following are selected:
      * generate parse tree listener
      * generate parse tree visitor
   4. Right-click the lexer file
   5. Generate ANTLR Recognizer
   6. Right-click the parser file
   7. Generate ANTLR Recognizer
3. Mark the `gen` directory as "Generated sources root"

You should be good to go.