package lexer;

public class Token {
    private final Lexeme lexeme;
    private int line;
    private int column;

    public Token(int line, int column) {
        this.lexeme = new Lexeme();
        this.line = line;
        this.column = column;
    }
    public Token(String lexeme, int line, int column) {
        this.lexeme = new Lexeme(lexeme);
        this.line = line;
        this.column = column;
    }

    public Token(char lexeme, Type type, int line, int column) {
        this.lexeme = new Lexeme(lexeme, type);
        this.line = line;
        this.column = column;
    }

    public Token(char lexeme, int line, int column) {
        this.lexeme = new Lexeme(lexeme);
        this.line = line;
        this.column = column;
    }

    public String getType() {
        return lexeme.getType();
    }

    public StringBuilder getLexeme() {
        return lexeme.getValue();
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void append(char character) {
        lexeme.getValue().append(character);
    }

    public void setType(Type type) {
        lexeme.setType(type);
    }

}
