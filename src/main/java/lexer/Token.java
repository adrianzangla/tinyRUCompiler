package lexer;

public class Token extends Word {
    final int line;
    final int column;

    public Token(String lexeme, Tag tag, int line, int column) {
        super(lexeme, tag);
        this.line = line;
        this.column = column;
    }

    public Token(Word word, int line, int column) {
        super(word.getLexeme(), word.getTag());
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

}
